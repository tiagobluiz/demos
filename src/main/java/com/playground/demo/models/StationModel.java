package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class StationModel {
    private final int id;
    private final Point coordinates;
    private final String address;
    private final Parish parish;
    private final StationStatus status;
    private final List<DockModel> docks = new ArrayList<>();
}
