package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StationModel {
    private int id;
    private double longitude;
    private double latitude;
    private String address;
    private Parish parish;
    private StationStatus status;
    @Builder.Default
    private List<DockModel> docks = new ArrayList<>();
}
