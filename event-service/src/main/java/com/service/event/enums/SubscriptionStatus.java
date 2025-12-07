package com.service.event.enums;

public enum SubscriptionStatus {
    PENDING("En attente"),
    APPROVED("Approuvé"),
    REJECTED("Rejeté"),
    CANCELLED("Annulé"),
    WAITLISTED("Liste d'attente"),
    ATTENDED("A assisté"),
    NO_SHOW("Absent"),
    EXPIRED("Expiré");

    private final String displayName;

    SubscriptionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == PENDING || this == APPROVED || this == WAITLISTED;
    }

    public boolean isCompleted() {
        return this == ATTENDED || this == NO_SHOW || this == EXPIRED;
    }
}