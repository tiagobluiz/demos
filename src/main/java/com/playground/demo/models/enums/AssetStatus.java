package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AssetStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_MAINTENANCE("On Maintenance"),
    REQUIRING_MAINTENANCE("Requiring Maintenance");

    @JsonProperty
    private final String text;
}
