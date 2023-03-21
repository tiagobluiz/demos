package com.playground.demo.models;

import com.playground.demo.persistence.entities.enums.AssetStatus;

public record DockModel(
        int id,
        AssetStatus status,
        BikeModel bike
) {
}
