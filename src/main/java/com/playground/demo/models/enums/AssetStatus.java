package com.playground.demo.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AssetStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_MAINTENANCE("On Maintenance"),
    REQUIRING_MAINTENANCE("Requiring Maintenance");

    @Getter
    private final String text;
}
