package at.ahwz.JRM.service;

import at.ahwz.JRM.model.JobApplication;
import at.ahwz.JRM.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void save(JobApplication jobApplication) {
        repository.save(jobApplication);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
