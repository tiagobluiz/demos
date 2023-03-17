package com.playground.demo.models;

import com.playground.demo.models.enums.BikeStatus;
import com.playground.demo.models.enums.BikeType;

public record BikeModel(
    int id,
    BikeType type,
    BikeStatus status,
    int km,
    double averageReview
) { }
