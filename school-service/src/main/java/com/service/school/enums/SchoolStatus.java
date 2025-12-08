package com.service.school.enums;

public enum SchoolStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING_VERIFICATION("Pending Verification"),
    CLOSED("Closed"),
    MERGED("Merged"),
    ACCREDITED("Accredited"),
    NON_ACCREDITED("Non-Accredited");

    private final String displayName;

    SchoolStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}