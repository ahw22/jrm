package at.ahwz.JRM.model;

public enum ApplicationStatus {
    APPLIED("Applied", "bg-primary", "#0d6efd"),
    INTERVIEWING("Interviewing", "bg-warning text-dark", "#ffc107"),
    OFFER("Offer", "bg-info text-dark", "#0dcaf0"),
    REJECTED("Rejected", "bg-danger", "#dc3545"),
    ACCEPTED("Accepted", "bg-success", "#198754");

    private final String label;
    private final String badgeClass;
    private final String color;

    ApplicationStatus(String label, String badgeClass, String color) {
        this.label = label;
        this.badgeClass = badgeClass;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public String getColor() {
        return color;
    }
}



