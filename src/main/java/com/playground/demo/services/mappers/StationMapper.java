package com.playground.demo.services.mappers;

import com.playground.demo.models.CreateStationRequest;
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
    StationModel mapToModel(StationEntity entity);

    List<StationModel> mapToModel(List<StationEntity> entity);

    @Mapping(target = "coordinates", expression = "java(GeometryUtils.GEOMETRY_FACTORY.createPoint(new Coordinate(createStationRequest.getLongitude(), createStationRequest.getLatitude())))")
    @Mapping(target = "docks", ignore = true)
    @Mapping(target = "id", ignore = true)
    StationEntity mapToEntity(CreateStationRequest createStationRequest);

    @Mapping(target = "coordinates", expression = "java(GeometryUtils.GEOMETRY_FACTORY.createPoint(new Coordinate(model.getLongitude(), model.getLatitude())))")
    StationEntity mapToEntity(StationModel model);

    List<com.playground.demo.persistence.entities.enums.StationStatus> mapStatusToEntity(List<StationStatus> status);
}
