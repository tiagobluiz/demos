package com.playground.demo.models;

import com.playground.demo.models.enums.DockStatus;

public record DockModel(
        int id,
        DockStatus status,
        BikeModel bike
) {
}
