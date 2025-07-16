package at.ahwz.JRM.repository;

import at.ahwz.JRM.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}
