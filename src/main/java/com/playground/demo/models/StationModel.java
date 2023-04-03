package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationModel {
    private int id;
    private double longitude;
    private double latitude;
    private String address;
    private Parish parish;
    private StationStatus status;
    private int electricBikesAvailable;
    private int classicBikesAvailable;
    private int freeDocks;
    private int totalDocks;
}
