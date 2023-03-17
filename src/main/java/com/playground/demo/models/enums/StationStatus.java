package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StationStatus {
    PLANNED("Planned"),
    INSTALLING("Installing"),
    TESTING("Testing"),
    INACTIVE("Inactive"),
    ON_MAINTENANCE("On Maintenance"),
    ACTIVE("Active");

    @JsonValue
    private final String text;
}
