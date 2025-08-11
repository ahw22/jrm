package at.ahwz.JRM.service;

import at.ahwz.JRM.model.ApplicationStatus;
import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository repository;

    public List<JobApplication> findAll() {
        return repository.findAll();
    }

    public JobApplication findById(Long id) throws RuntimeException {
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

    public JobApplication save(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
