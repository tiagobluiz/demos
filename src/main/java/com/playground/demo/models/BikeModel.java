package com.playground.demo.models;

import com.playground.demo.models.enums.AssetStatus;
import com.playground.demo.models.enums.BikeType;

public record BikeModel(
    int id,
    BikeType type,
    AssetStatus status,
    int km,
    double averageReview
) { }
