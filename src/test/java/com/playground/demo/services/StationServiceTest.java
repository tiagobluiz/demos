package com.playground.demo.services;

import com.playground.demo.exceptions.notfound.StationNotFoundException;
import com.playground.demo.models.CreateStationRequest;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.StationsSearchCriteria;
import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.persistence.repositories.StationRepository;
import com.playground.demo.services.mappers.StationMapper;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.playground.demo.persistence.entities.enums.Parish.LUMIAR;
import static com.playground.demo.persistence.entities.enums.StationStatus.ACTIVE;
import static com.playground.demo.utils.TestUtils.GEOMETRY_FACTORY;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    private final static EasyRandom OBJECT_GENERATOR;

    static {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(1L)
                .excludeField(field -> field.getName().equals("coordinates"))
                .excludeField(field -> field.getName().equals("docks"))
                .stringLengthRange(3, 10)
                .collectionSizeRange(1, 15);
        OBJECT_GENERATOR = new EasyRandom(parameters);
    }

    public StationMapper stationMapper = Mappers.getMapper(StationMapper.class);
    @Mock
    private StationRepository stationRepository;

    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationService(stationRepository, stationMapper);
    }

    @Test
    void givenAValidId_whenGettingAStation_thenStationIsReturned() {
        // given
        final var stationEntity = StationEntity.builder()
                .id(OBJECT_GENERATOR.nextInt())
                .coordinates(GEOMETRY_FACTORY.createPoint())
                .address("ADDRESS")
                .status(ACTIVE)
                .parish(LUMIAR)
                .build();
        when(stationRepository.findById(stationEntity.getId())).thenReturn(Optional.of(stationEntity));

        // when
        final var actualStation = stationService.getStation(stationEntity.getId());

        // then
        final var expectedStation = StationModel.builder()
                .id(stationEntity.getId())
                .coordinates(stationEntity.getCoordinates())
                .address(stationEntity.getAddress())
                .status(StationStatus.ACTIVE)
                .parish(Parish.LUMIAR)
                .build();

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
        final var entities = OBJECT_GENERATOR.objects(StationEntity.class, 3).toList();
        when(stationRepository.findAllInRadius(eq(null), eq(null), any())).thenReturn(entities);

        final var searchCriteria = StationsSearchCriteria.builder()
                .statuses(asList(StationStatus.values()))
                .build();

        // when
        final var actualStations = stationService.getAllStations(searchCriteria);

        // then
        assertThat(actualStations).isNotNull();
        assertThat(actualStations.getStations()).isNotNull();

        final var entitiesIds = entities.stream().map(StationEntity::getId).toList();
        assertThat(actualStations.getStations())
                .extracting(StationModel::getId)
                .containsExactlyInAnyOrderElementsOf(entitiesIds);
    }

    @Test
    void givenThatNoStationExists_whenGettingAllStations_thenEmptyListIsReturned() {
        // given
        when(stationRepository.findAllInRadius(any(), any(), any())).thenReturn(List.of());

        // when
        final var actualStations = stationService.getAllStations(new StationsSearchCriteria());

        // then
        assertThat(actualStations).isNotNull();
        assertThat(actualStations.getStations()).isEmpty();
    }

    @Test
    void givenThatStationsExists_whenGettingAllStationsWithinARadius_thenListOfStationsWithinRadiusIsReturned() {
        // given
        final var entities = OBJECT_GENERATOR.objects(StationEntity.class, 3).toList();
        final var coordinatesAsPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(38.77066006800165, -9.160284356927665));

        final var searchCriteria = StationsSearchCriteria.builder()
                .longitude(coordinatesAsPoint.getX())
                .latitude(coordinatesAsPoint.getY())
                .radius(50)
                .statuses(Arrays.asList(StationStatus.values()))
                .build();

        when(stationRepository.findAllInRadius(
                coordinatesAsPoint,
                50,
                Arrays.asList(com.playground.demo.persistence.entities.enums.StationStatus.values()))
        ).thenReturn(entities);

        // when
        final var actualStations = stationService.getAllStations(searchCriteria);

        // then
        assertThat(actualStations).isNotNull();
        assertThat(actualStations.getStations()).isNotNull();

        final var entitiesIds = entities.stream().map(StationEntity::getId).toList();
        assertThat(actualStations.getStations())
                .extracting(StationModel::getId)
                .containsExactlyInAnyOrderElementsOf(entitiesIds);
    }

    @Test
    void givenNoStationsWithinRadius_whenGettingAllStationsWithinARadius_thenListOfStationsIsEmpty() {
        // given
        final var coordinatesAsPoint = GEOMETRY_FACTORY.createPoint(new Coordinate(0, 0));
        when(stationRepository.findAllInRadius(eq(coordinatesAsPoint), eq(50), any())).thenReturn(List.of());

        final var searchCriteria = StationsSearchCriteria.builder()
                .longitude(coordinatesAsPoint.getX())
                .latitude(coordinatesAsPoint.getY())
                .radius(50)
                .statuses(Arrays.asList(StationStatus.values()))
                .build();

        // when
        final var actualStations = stationService.getAllStations(searchCriteria);

        // then
        assertThat(actualStations).isNotNull();
        assertThat(actualStations.getStations()).isEmpty();
    }

    @Test
    void givenValidArguments_whenCreatingAStation_thenCreatedStationIsReturned() {
        // given
        final var entity = StationEntity.builder()
                .coordinates(GEOMETRY_FACTORY.createPoint(new Coordinate(0, 0)))
                .address("ADDRESS")
                .status(ACTIVE)
                .parish(LUMIAR)
                .docks(List.of())
                .build();

        doAnswer(answer -> entity.setId(OBJECT_GENERATOR.nextInt())).when(stationRepository).save(entity);

        final var createRequest = CreateStationRequest.builder()
                .address("ADDRESS")
                .longitude(entity.getCoordinates().getX())
                .longitude(entity.getCoordinates().getY())
                .status(StationStatus.ACTIVE)
                .parish(Parish.LUMIAR)
                .build();

        // when
        final var createdStation = stationService.createStation(createRequest);

        // then
        final var expectedStation = StationModel.builder()
                .id(entity.getId())
                .coordinates(entity.getCoordinates())
                .address(entity.getAddress())
                .status(StationStatus.ACTIVE)
                .parish(Parish.LUMIAR)
                .build();

        assertThat(createdStation).isEqualTo(expectedStation);
    }

    @Test
    void givenValidArguments_whenUpdatingStation_thenUpdatedStationIsReturned() {
        final var updatedModel = StationModel.builder()
                .id(OBJECT_GENERATOR.nextInt())
                .coordinates(GEOMETRY_FACTORY.createPoint(new Coordinate(0, 0)))
                .address("ADDRESS")
                .status(StationStatus.ACTIVE)
                .parish(Parish.LUMIAR)
                .build();

        final var objectBeforeUpdate = StationEntity.builder()
                .id(updatedModel.getId())
                .coordinates(updatedModel.getCoordinates())
                .address(updatedModel.getAddress())
                .status(ACTIVE)
                .parish(LUMIAR)
                .build();

        when(stationRepository.findById(updatedModel.getId())).thenReturn(Optional.of(objectBeforeUpdate));

        // when
        final var actualResult = stationService.updateStation(updatedModel.getId(), updatedModel);

        // then
        assertThat(actualResult).isEqualTo(updatedModel);
    }

    @Test
    void givenThatStationDoesNotExist_whenUpdatingStation_thenStationNotFoundIsThrown() {
        // given
        final var randomStation = OBJECT_GENERATOR.nextObject(StationModel.class);
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