package com.playground.demo.services;

import com.playground.demo.exceptions.notfound.StationNotFoundException;
import com.playground.demo.persistence.entities.DockEntity;
import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.repositories.StationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StationService {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    private final StationRepository stationRepository;

    @NonNull
    public StationEntity getStation(int stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }

    @NonNull
    public List<StationEntity> getAllStations() {
        return stationRepository.findAll();
    }

    @NonNull
    public List<StationEntity> getAllStationsWithinRadius(double longitude, double latitude, int radiusInMeters) {
        final var coordinatesAsPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
        return stationRepository.findAllInRadius(coordinatesAsPoint, radiusInMeters);
    }

    @Transactional
    @NonNull
    public StationEntity createStation(StationEntity stationToCreate, int docksToCreate) {
        final var docks = IntStream.range(0, docksToCreate)
                .mapToObj(number -> buildDock(stationToCreate, number))
                .toList();

        stationToCreate.setDocks(docks);

        return stationRepository.save(stationToCreate);
    }

    private DockEntity buildDock(StationEntity station, int number) {
        return DockEntity.builder()
                .station(station)
                .number(number)
                .status(AssetStatus.INACTIVE)
                .build();
    }

    @Transactional
    @NonNull
    public StationEntity updateStation(int stationId, StationEntity stationToUpdate) {
        final var station = getStation(stationId);

        return station.setStatus(stationToUpdate.getStatus())
                .setCoordinates(stationToUpdate.getCoordinates())
                .setAddress(stationToUpdate.getAddress())
                .setParish(stationToUpdate.getParish());
    }
}
