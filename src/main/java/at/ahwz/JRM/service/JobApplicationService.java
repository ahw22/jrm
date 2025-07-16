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

    public void save(JobApplication jobApplication) {
        repository.save(jobApplication);
    }
}
