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
}
