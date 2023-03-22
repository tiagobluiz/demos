package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateStationRequest {
    private double longitude;
    private double latitude;
    private String address;
    private Parish parish;
    private StationStatus status;
    private int docks;
}
