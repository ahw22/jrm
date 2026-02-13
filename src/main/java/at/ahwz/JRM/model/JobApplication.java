package at.ahwz.JRM.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    private String id;
    private String companyName;
    private String position;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private String notes;
    private List<String> advertImageFilenames = new ArrayList<>();

    public boolean isActive() {
        return status.equals(ApplicationStatus.APPLIED) || status.equals(ApplicationStatus.INTERVIEWING) || status.equals(ApplicationStatus.OFFER);
    }

    public boolean isStale() {
        return appliedDate.isBefore(LocalDate.now().minusDays(30));
    }

    public void addAdvertImageFilename(String name) {
        if (advertImageFilenames == null) advertImageFilenames = new ArrayList<>();
        advertImageFilenames.add(name);
    }

    public void removeAdvertImageFilename(String name) {
        if (advertImageFilenames == null) return;
        advertImageFilenames.remove(name);
    }
}
