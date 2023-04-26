package com.playground.demo.services.mappers;

import com.playground.demo.models.BikeModel;
import com.playground.demo.models.BikeRequest;
import com.playground.demo.persistence.entities.BikeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.time.LocalDate;

@Mapper(imports = {LocalDate.class}, componentModel = "spring",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface BikeMapper {

    BikeModel mapToModel(final BikeEntity entity);

    // todo:map average review once it is implemented
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dock", ignore = true)
    @Mapping(target = "kms", ignore = true)
    @Mapping(target = "lastMaintenanceDate", expression = "java(LocalDate.now())")
    BikeEntity mapToEntity(final BikeRequest bikeRequest);

    @Mapping(target = "id", source = "bikeId")
    @Mapping(target = "dock", ignore = true)
    BikeEntity mapToEntity(final int bikeId, final BikeRequest bikeRequest);
}
