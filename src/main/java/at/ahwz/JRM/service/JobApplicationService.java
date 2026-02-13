package at.ahwz.JRM.service;

import at.ahwz.JRM.model.ApplicationStatus;
import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository repository;

    private final String uploadDir = "uploads";

    public List<JobApplication> findAll() {
        return repository.findAll();
    }

    public JobApplication findById(String id) throws RuntimeException {
        try {
            return repository.findById(id).get();
        } catch (Exception e) {
            throw new RuntimeException("No application with given ID found.");
        }
    }

    public List<JobApplication> findAllActive() {
        List<JobApplication> applications = repository.findAll();
        return applications.stream().filter(JobApplication::isActive).toList();
    }

    public void getDashboardData(Model model) {
        List<JobApplication> applications = findAll();
        List<JobApplication> activeApplications = findAllActive();

        Map<ApplicationStatus, Long> statusCounts = applications.stream()
                .collect(Collectors.groupingBy(JobApplication::getStatus, Collectors.counting()));
        Map<String, String> statusColors = Arrays.stream(ApplicationStatus.values())
                .collect(Collectors.toMap(Enum::name, ApplicationStatus::getColor));

        model.addAttribute("statusColors", statusColors);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("applications", applications);
        model.addAttribute("activeApplications", activeApplications);
        model.addAttribute("staleCount", activeApplications.stream().filter(JobApplication::isStale).toList().size());
    }

    public JobApplication saveApplication(JobApplication jobApplication,
                                          MultipartFile[] imageFiles,
                                          String[] removeImages) throws IOException {

        // 1. Ensure MongoDB generates an _id if necessary
        if (jobApplication.getId() == null || jobApplication.getId().isBlank()) {
            jobApplication.setId(null);
        }

        // 2. Remove images marked for deletion
        if (removeImages != null) {
            for (String filename : removeImages) {
                jobApplication.removeAdvertImageFilename(filename);
                Path filePath = Paths.get(uploadDir).resolve(filename);
                Files.deleteIfExists(filePath);
            }
        }

        // 3. Save new uploaded images
        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    jobApplication.addAdvertImageFilename(fileName);
                }
            }
        }

        // 4. Save the entity
        return repository.save(jobApplication);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
