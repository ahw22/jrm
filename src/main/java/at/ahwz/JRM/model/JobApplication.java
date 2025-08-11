package at.ahwz.JRM.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String position;
    private LocalDate appliedDate;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private String notes;
    private String advertImageFilename;

    public boolean isActive() {
        return status.equals(ApplicationStatus.APPLIED) || status.equals(ApplicationStatus.INTERVIEWING) || status.equals(ApplicationStatus.OFFER);
    }

    public boolean isStale() {
        return appliedDate.isBefore(LocalDate.now().minusDays(30));
    }
}
