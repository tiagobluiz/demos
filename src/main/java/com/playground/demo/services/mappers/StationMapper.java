package com.playground.demo.services.mappers;

import com.playground.demo.models.StationRequest;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.enums.StationStatus;
import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.utils.GeometryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(imports = {GeometryUtils.class, Coordinate.class}, componentModel = "spring",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface StationMapper {

    @Mapping(target = "longitude", source = "entity.coordinates.x")
    @Mapping(target = "latitude", source = "entity.coordinates.y")
    StationModel mapToModel(final StationEntity entity);

    List<StationModel> mapToModel(final List<StationEntity> entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "docks", ignore = true)
    @Mapping(target = "coordinates", expression = "java(GeometryUtils.GEOMETRY_FACTORY.createPoint(new Coordinate(stationRequest.getLongitude(), stationRequest.getLatitude())))")
    StationEntity mapToEntity(final StationRequest stationRequest);

    @Mapping(target = "docks", ignore = true)
    @Mapping(target = "id", source = "stationId")
    @Mapping(target = "coordinates", expression = "java(GeometryUtils.GEOMETRY_FACTORY.createPoint(new Coordinate(stationRequest.getLongitude(), stationRequest.getLatitude())))")
    StationEntity mapToEntity(final int stationId, final StationRequest stationRequest);

    List<com.playground.demo.persistence.entities.enums.StationStatus> mapStatusToEntity(final List<StationStatus> status);
}
