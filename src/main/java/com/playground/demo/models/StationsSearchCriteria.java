package com.playground.demo.models;

import com.playground.demo.models.enums.StationStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import java.util.List;

import static com.playground.demo.utils.GeometryUtils.GEOMETRY_FACTORY;
import static java.util.Objects.nonNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StationsSearchCriteria {

    @Nullable
    private Double longitude;

    @Nullable
    private Double latitude;

    @Nullable
    private Integer radius;

    @NotNull
    @Builder.Default
    private List<StationStatus> statuses = List.of(StationStatus.ACTIVE);

    public Point getCoordinatesAsPoint() {
        return nonNull(longitude) && nonNull(latitude) && nonNull(radius)
                ? GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude))
                : null;
    }
}
