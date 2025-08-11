package at.ahwz.JRM.controller;

import at.ahwz.JRM.model.ApplicationStatus;
import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    @GetMapping("/")
    public String listApplications(Model model) {
        service.getDashboardData(model);
        return "index";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        model.addAttribute("statuses", ApplicationStatus.values());
        return "form";
    }

    @PostMapping("/save")
    public String saveApplication(@ModelAttribute JobApplication jobApplication,
                                  @RequestParam("advertImage") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String uploadDir = "uploads/";

            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            jobApplication.setAdvertImageFilename(fileName);
        }
        JobApplication savedApplication = service.save(jobApplication);
        return "redirect:/application/" + savedApplication.getId();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        JobApplication jobApplication = service.findById(id);
        model.addAttribute("jobApplication", jobApplication);
        model.addAttribute("statuses", ApplicationStatus.values());
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id) {
         service.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/application/{id}")
    public String getApplication(@PathVariable Long id, Model model) {
        JobApplication application = service.findById(id);
        model.addAttribute("jobApplication", application);
        return "application";
    }
}
