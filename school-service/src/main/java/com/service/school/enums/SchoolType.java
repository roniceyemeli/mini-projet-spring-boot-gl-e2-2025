package com.service.school.enums;

public enum SchoolType {
    UNIVERSITY("University"),
    COLLEGE("College"),
    HIGH_SCHOOL("High School"),
    MIDDLE_SCHOOL("Middle School"),
    ELEMENTARY_SCHOOL("Elementary School"),
    VOCATIONAL("Vocational School"),
    LANGUAGE_SCHOOL("Language School"),
    TECHNICAL("Technical School"),
    BUSINESS("Business School"),
    MEDICAL("Medical School"),
    LAW("Law School"),
    ART("Art School"),
    MUSIC("Music School"),
    ONLINE("Online School"),
    INTERNATIONAL("International School"),
    PRIVATE("Private School"),
    PUBLIC("Public School");

    private final String displayName;

    SchoolType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}