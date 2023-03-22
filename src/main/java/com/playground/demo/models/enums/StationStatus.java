package com.playground.demo.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StationStatus {
    PLANNED("Planned"),
    INSTALLING("Installing"),
    TESTING("Testing"),
    INACTIVE("Inactive"),
    ON_MAINTENANCE("On Maintenance"),
    ACTIVE("Active");

    @Getter
    private final String text;
}
