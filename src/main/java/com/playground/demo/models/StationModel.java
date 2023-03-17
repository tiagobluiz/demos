package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StationModel {
    private final int id;
    private final String coordinates;
    private final String address;
    private final Parish parish;
    private final StationStatus status;
    private final List<DockModel> docks;
}
