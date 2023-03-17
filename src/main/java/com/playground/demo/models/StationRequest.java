package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;

public record StationRequest(
        String coordinates,
        String address,
        Parish parish,
        StationStatus status,
        int docks
) {
}
