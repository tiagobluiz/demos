package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DockStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_MAINTENANCE("On Maintenance"),
    REQUIRING_MAINTENANCE("Requiring Maintenance");

    @JsonValue
    private final String text;
}
