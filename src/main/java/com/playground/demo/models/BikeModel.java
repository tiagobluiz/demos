package com.playground.demo.models;

import com.playground.demo.models.enums.AssetStatus;
import com.playground.demo.models.enums.BikeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BikeModel {

    private int id;
    private BikeType type;
    private AssetStatus status;
    private int kms;
    private LocalDate lastMaintenanceDate;
    private double averageReview;
}

