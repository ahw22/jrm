package at.ahwz.JRM.controller;

import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    @GetMapping("/")
    public String listApplications(Model model) {
        model.addAttribute("applications", service.findAll());
        return "index";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        return "form";
    }

    @PostMapping("/save")
    public String save(JobApplication jobApplication) {
        service.save(jobApplication);
        return "redirect:/";
    }
}
