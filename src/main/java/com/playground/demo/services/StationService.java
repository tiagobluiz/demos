package com.playground.demo.services;

import com.playground.demo.exceptions.notfound.StationNotFoundException;
import com.playground.demo.models.CreateStationRequest;
import com.playground.demo.models.NearStationsModel;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.StationsSearchCriteria;
import com.playground.demo.persistence.entities.DockEntity;
import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.repositories.StationRepository;
import com.playground.demo.services.mappers.StationMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    private final StationMapper stationMapper;

    @NotNull
    @Transactional(readOnly = true)
    public StationModel getStation(int stationId) {
        final var station = getStationAsEntity(stationId);

        return stationMapper.mapToModel(station);
    }

    private StationEntity getStationAsEntity(final int stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }

    @NotNull
    @Transactional(readOnly = true)
    public NearStationsModel getAllStations(final StationsSearchCriteria searchCriteria) {
        final var statuses = stationMapper.mapStatusToEntity(searchCriteria.getStatuses());
        final var stationsEntities = stationRepository.findAllInRadius(
                searchCriteria.getCoordinatesAsPoint(),
                searchCriteria.getRadius(),
                statuses
        );

        final var stations = stationMapper.mapToModel(stationsEntities);

        return new NearStationsModel(stations);
    }

    @Transactional
    @NotNull
    public StationModel createStation(final CreateStationRequest stationToCreate) {
        final var stationEntity = stationMapper.mapToEntity(stationToCreate);

        final var docks = IntStream.range(0, stationToCreate.getDocks())
                .mapToObj(number -> buildDock(stationEntity, number))
                .toList();

        stationEntity.setDocks(docks);

        final var entity = stationRepository.save(stationEntity);

        return stationMapper.mapToModel(entity);
    }

    private DockEntity buildDock(final StationEntity station, final int number) {
        return DockEntity.builder()
                .station(station)
                .number(number)
                .status(AssetStatus.INACTIVE)
                .build();
    }

    @Transactional
    @NotNull
    public StationModel updateStation(final int stationId, final StationModel stationToUpdate) {
        final var station = getStationAsEntity(stationId);

        final var modelAsEntity = stationMapper.mapToEntity(stationToUpdate);

        station.setStatus(modelAsEntity.getStatus())
                .setCoordinates(modelAsEntity.getCoordinates())
                .setAddress(modelAsEntity.getAddress())
                .setParish(modelAsEntity.getParish());

        return stationMapper.mapToModel(station);
    }
}
