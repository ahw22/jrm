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
    public String saveApplication(
            @ModelAttribute JobApplication jobApplication,
            @RequestParam(value = "advertImages", required = false) MultipartFile[] imageFiles,
            @RequestParam(value = "removeImages", required = false) String[] removeImages) throws IOException {

        JobApplication savedApplication = service.saveApplication(jobApplication, imageFiles, removeImages);

        return "redirect:/application/" + savedApplication.getId();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        JobApplication jobApplication = service.findById(id);
        model.addAttribute("jobApplication", jobApplication);
        model.addAttribute("statuses", ApplicationStatus.values());
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String deleteApplication(@PathVariable String id) {
         service.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/application/{id}")
    public String getApplication(@PathVariable String id, Model model) {
        JobApplication application = service.findById(id);
        model.addAttribute("jobApplication", application);
        return "application";
    }
}
