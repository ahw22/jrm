package at.ahwz.JRM.repository;

import at.ahwz.JRM.model.JobApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobApplicationRepository extends MongoRepository<JobApplication, String> {
}
