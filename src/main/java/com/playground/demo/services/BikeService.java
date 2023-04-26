package com.playground.demo.services;

import com.playground.demo.exceptions.notfound.BikeNotFoundException;
import com.playground.demo.models.BikeModel;
import com.playground.demo.models.BikeRequest;
import com.playground.demo.persistence.entities.BikeEntity;
import com.playground.demo.persistence.repositories.BikeRepository;
import com.playground.demo.services.mappers.BikeMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BikeService {

    private final BikeRepository bikeRepository;

    private final BikeMapper bikeMapper;

    @NotNull
    private BikeEntity getBikeAsEntity(final int bikeId) {
        return bikeRepository.findById(bikeId)
                .orElseThrow(() -> new BikeNotFoundException(bikeId));
    }

    @NotNull
    @Transactional(readOnly = true)
    public BikeModel getBike(final int bikeId) {
        final var bike = getBikeAsEntity(bikeId);

        return bikeMapper.mapToModel(bike);
    }

    @NotNull
    @Transactional
    public BikeModel createBike(@NotNull final BikeRequest bikeToCreate) {
        log.info("Creating a new bike with the following parameters: {}", bikeToCreate);

        final var bikeEntity = bikeMapper.mapToEntity(bikeToCreate);

        final var entity = bikeRepository.save(bikeEntity);

        log.info("Bike with identifier {} was creates", entity.getId());

        return bikeMapper.mapToModel(entity);
    }

    @NotNull
    @Transactional
    public BikeModel updateBike(final int bikeId, @NotNull final BikeRequest bikeToUpdate) {
        log.info("Updating bike {} with the following parameters: {}", bikeId, bikeToUpdate);

        final var bike = getBikeAsEntity(bikeId);

        final var entityWithRequestedChanges = bikeMapper.mapToEntity(bikeId, bikeToUpdate);

        bike.setType(entityWithRequestedChanges.getType())
                .setStatus(entityWithRequestedChanges.getStatus())
                .setKms(entityWithRequestedChanges.getKms())
                .setLastMaintenanceDate(entityWithRequestedChanges.getLastMaintenanceDate());

        return bikeMapper.mapToModel(bike);
    }
}
