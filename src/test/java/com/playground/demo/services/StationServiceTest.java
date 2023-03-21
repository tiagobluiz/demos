package com.playground.demo.services;

import com.playground.demo.exceptions.notfound.StationNotFoundException;
import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.persistence.entities.enums.StationStatus;
import com.playground.demo.persistence.repositories.StationRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);
    private final static EasyRandom OBJECT_GENERATOR;


    static {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .excludeField(field -> field.getName().equals("coordinates"))
                .stringLengthRange(3, 10)
                .collectionSizeRange(1, 15);
        OBJECT_GENERATOR = new EasyRandom(parameters);
    }

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @Test
    void givenAValidId_whenGettingAStation_thenStationIsReturned() {
        // given
        final var expectedStation = OBJECT_GENERATOR.nextObject(StationEntity.class);
        when(stationRepository.findById(expectedStation.getId())).thenReturn(Optional.of(expectedStation));

        // when
        final var actualStation = stationService.getStation(expectedStation.getId());

        // then
        assertThat(actualStation).isEqualTo(expectedStation);
    }

    @Test
    void givenThatStationDoesNotExist_whenGettingAStation_thenStationNotFoundExceptionIsThrown() {
        // given
        when(stationRepository.findById(any())).thenReturn(Optional.empty());

        // when
        final var exception = assertThrowsExactly(
                StationNotFoundException.class,
                () -> stationService.getStation(new Random().nextInt())
        );

        // then
        assertThat(exception.getReason())
                .isEqualTo("The requested station does not exist, please verify that the identifier is correct.");
    }

    @Test
    void givenThatStationsExists_whenGettingAllStations_thenListOfStationsIsReturned() {
        // given
        final var expectedStations = OBJECT_GENERATOR.objects(StationEntity.class, 3).toList();
        when(stationRepository.findAll()).thenReturn(expectedStations);

        // when
        final var actualStations = stationService.getAllStations();

        // then
        assertThat(actualStations).containsExactlyElementsOf(expectedStations);
    }

    @Test
    void givenThatNoStationExists_whenGettingAllStations_thenEmptyListIsReturned() {
        // given
        when(stationRepository.findAll()).thenReturn(List.of());

        // when
        final var actualStations = stationService.getAllStations();

        // then
        assertThat(actualStations).isEmpty();
    }

    @Test
    void givenThatStationsExists_whenGettingAllStationsWithinARadius_thenListOfStationsWithinRadiusIsReturned() {
        // given
        final var expectedStations = OBJECT_GENERATOR.objects(StationEntity.class, 3).toList();
        final var coordinatesAsPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(38.77066006800165, -9.160284356927665));
        when(stationRepository.findAllInRadius(coordinatesAsPoint, 50)).thenReturn(expectedStations);

        // when
        final var actualStations = stationService.getAllStationsWithinRadius(
                coordinatesAsPoint.getX(),
                coordinatesAsPoint.getY(),
                50
        );

        // then
        assertThat(actualStations).containsExactlyElementsOf(expectedStations);
    }

    @Test
    void givenNoStationsWithinRadius_whenGettingAllStationsWithinARadius_thenListOfStationsIsEmpty() {
        // given
        final var coordinatesAsPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(38.77066006800165, -9.160284356927665));
        when(stationRepository.findAllInRadius(coordinatesAsPoint, 50)).thenReturn(List.of());

        // when
        final var actualStations = stationService.getAllStationsWithinRadius(
                coordinatesAsPoint.getX(),
                coordinatesAsPoint.getY(),
                50
        );

        // then
        assertThat(actualStations).isEmpty();
    }

    @Test
    void givenValidArguments_whenCreatingAStation_thenCreatedStationIsReturned() {
        // given
        final var expectedResult = OBJECT_GENERATOR.nextObject(StationEntity.class);
        when(stationRepository.save(expectedResult)).thenReturn(expectedResult);

        // when
        final var actualResult = stationService.createStation(expectedResult, 5);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void givenValidArguments_whenUpdatingStation_thenUpdatedStationIsReturned() {
        final var expectedResult = OBJECT_GENERATOR.nextObject(StationEntity.class);
        final var objectBeforeUpdate = StationEntity.builder()
                .id(expectedResult.getId())
                .address(OBJECT_GENERATOR.nextObject(String.class))
                .coordinates(expectedResult.getCoordinates())
                .parish(expectedResult.getParish())
                .status(OBJECT_GENERATOR.nextObject(StationStatus.class))
                .docks(expectedResult.getDocks())
                .build();

        when(stationRepository.findById(expectedResult.getId())).thenReturn(Optional.of(objectBeforeUpdate));

        // when
        final var actualResult = stationService.updateStation(expectedResult.getId(), expectedResult);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void givenThatStationDoesNotExist_whenUpdatingStation_thenStationNotFoundIsThrown() {
        // given
        final var randomStation = OBJECT_GENERATOR.nextObject(StationEntity.class);
        when(stationRepository.findById(any())).thenReturn(Optional.empty());

        // when
        final var exception = assertThrowsExactly(
                StationNotFoundException.class,
                () -> stationService.updateStation(randomStation.getId(), randomStation)
        );

        // then
        assertThat(exception.getReason())
                .isEqualTo("The requested station does not exist, please verify that the identifier is correct.");
    }
}