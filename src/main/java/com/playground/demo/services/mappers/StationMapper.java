package com.playground.demo.services.mappers;

import com.playground.demo.models.CreateStationRequest;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.enums.StationStatus;
import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.utils.GeometryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(imports = {GeometryUtils.class, Coordinate.class})
public interface StationMapper {

    StationModel mapToModel(StationEntity entity);

    List<StationModel> mapToModel(List<StationEntity> entity);

    @Mapping(target = "coordinates", expression = "java(GeometryUtils.GEOMETRY_FACTORY.createPoint(new Coordinate(createStationRequest.getLongitude(), createStationRequest.getLatitude())))")
    @Mapping(target = "docks", ignore = true)
    StationEntity mapToEntity(CreateStationRequest createStationRequest);

    StationEntity mapToEntity(StationModel createStationRequest);

    List<com.playground.demo.persistence.entities.enums.StationStatus> mapStatusToEntity(List<StationStatus> status);
}
