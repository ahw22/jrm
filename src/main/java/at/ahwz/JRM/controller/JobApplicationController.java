package at.ahwz.JRM.controller;

import at.ahwz.JRM.model.ApplicationStatus;
import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    @GetMapping("/")
    public String listApplications(Model model) {
        List<JobApplication> applications = service.findAll();

        Map<ApplicationStatus, Long> statusCounts = applications.stream()
                .collect(Collectors.groupingBy(JobApplication::getStatus, Collectors.counting()));
        Map<String, String> statusColors = Arrays.stream(ApplicationStatus.values())
                .collect(Collectors.toMap(Enum::name, ApplicationStatus::getColor));

        model.addAttribute("statusColors", statusColors);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("applications", applications);
        return "index";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        model.addAttribute("statuses", ApplicationStatus.values());
        return "form";
    }

    @PostMapping("/save")
    public String save(JobApplication jobApplication) {
        service.save(jobApplication);
        return "redirect:/";
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

    @GetMapping("/stats")
    public String showStats(Model model) {
        List<JobApplication> applications = service.findAll();

        Map<ApplicationStatus, Long> statusCounts = applications.stream()
                .collect(Collectors.groupingBy(JobApplication::getStatus, Collectors.counting()));

        model.addAttribute("statusCounts", statusCounts);
        return "stats";
    }

}
