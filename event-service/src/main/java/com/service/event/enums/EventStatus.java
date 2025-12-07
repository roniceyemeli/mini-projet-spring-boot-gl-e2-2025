package com.service.event.enums;

public enum EventStatus {
    DRAFT("Draft"),
    ACTIVE("Active"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    ARCHIVED("Archived");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}