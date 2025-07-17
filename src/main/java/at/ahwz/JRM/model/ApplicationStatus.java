package at.ahwz.JRM.model;

import lombok.Getter;

public enum ApplicationStatus {
    APPLIED("Applied", "bg-primary"),
    INTERVIEWING("Interviewing", "bg-warning text-dark"),
    OFFER("Offer", "bg-info text-dark"),
    REJECTED("Rejected", "bg-danger"),
    ACCEPTED("Accepted", "bg-success");

    private final String label;
    private final String badgeClass;

    ApplicationStatus(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}


