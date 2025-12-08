package com.service.student.enums;

public enum EnrollmentStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    GRADUATED("Graduated"),
    WITHDRAWN("Withdrawn"),
    ON_LEAVE("On Leave"),
    PROBATION("Probation");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}