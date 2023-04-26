package com.playground.demo.services;

import com.playground.demo.exceptions.notfound.BikeNotFoundException;
import com.playground.demo.models.BikeModel;
import com.playground.demo.models.BikeRequest;
import com.playground.demo.persistence.entities.BikeEntity;
import com.playground.demo.persistence.entities.DockEntity;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.entities.enums.BikeType;
import com.playground.demo.persistence.repositories.BikeRepository;
import com.playground.demo.services.mappers.BikeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import static com.playground.demo.models.enums.AssetStatus.ACTIVE;
import static com.playground.demo.models.enums.AssetStatus.INACTIVE;
import static com.playground.demo.models.enums.BikeType.ELECTRIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BikeServiceTest {

    // Reoccurring data
    private static final BikeEntity BIKE_ENTITY = BikeEntity.builder()
            .id(1)
            .type(BikeType.ELECTRIC)
            .status(AssetStatus.ACTIVE)
            .kms(111)
            .dock(DockEntity.builder().build())
            .build();

    private static final BikeModel BIKE_MODEL = BikeModel.builder()
            .id(BIKE_ENTITY.getId())
            .type(ELECTRIC)
            .status(ACTIVE)
            .kms(BIKE_ENTITY.getKms())
            .averageReview(0d)
            .build();

    private final BikeMapper bikeMapper = Mappers.getMapper(BikeMapper.class);

    @Mock
    private BikeRepository bikeRepository;

    private BikeService bikeService;

    @BeforeEach
    void setUp() {
        bikeService = new BikeService(bikeRepository, bikeMapper);
    }

    @Test
    void givenAValidId_whenGettingABike_thenBikeIsReturned() {
        // given
        when(bikeRepository.findById(BIKE_ENTITY.getId())).thenReturn(Optional.of(BIKE_ENTITY));

        // when
        final var actualBike = bikeService.getBike(BIKE_ENTITY.getId());

        // then
        assertThat(actualBike).isEqualTo(BIKE_MODEL);
    }

    @Test
    void givenAnInvalidId_whenGettingABike_thenBikeNotFoundIsThrown() {
        // given
        when(bikeRepository.findById(any())).thenReturn(Optional.empty());

        // when
        final var exception = assertThrowsExactly(
                BikeNotFoundException.class,
                () -> bikeService.getBike(BIKE_ENTITY.getId())
        );

        // then
        assertThat(exception.getReason())
                .isEqualTo("The requested bike does not exist, please verify that the identifier is correct.");
    }

    @Test
    void givenValidArguments_whenCreatingABike_thenCreatedBikeIsReturned() {
        // given
        final var saveInput = BikeEntity.builder()
                .type(BikeType.ELECTRIC)
                .status(AssetStatus.ACTIVE)
                .lastMaintenanceDate(LocalDate.now())
                .build();

        final var createRequest = BikeRequest.builder()
                .type(ELECTRIC)
                .status(ACTIVE)
                .build();

        doAnswer(answer -> saveInput.setId(new Random().nextInt())).when(bikeRepository).save(saveInput);

        // when
        final var createdBike = bikeService.createBike(createRequest);

        // then
        final var expectedBike = BikeModel.builder()
                .id(saveInput.getId())
                .type(ELECTRIC)
                .status(ACTIVE)
                .lastMaintenanceDate(saveInput.getLastMaintenanceDate())
                .build();

        assertThat(createdBike).isEqualTo(expectedBike);
    }

    @Test
    void givenValidArguments_whenUpdatingABike_thenUpdatedBikeIsReturned() {
        // given
        final var updateRequest = BikeRequest.builder()
                .type(ELECTRIC)
                .status(INACTIVE)
                .kms(1234)
                .build();
        when(bikeRepository.findById(BIKE_ENTITY.getId())).thenReturn(Optional.of(BIKE_ENTITY));

        // when
        final var updatedBike = bikeService.updateBike(BIKE_ENTITY.getId(), updateRequest);

        // then
        final var expectedBike = BikeModel.builder()
                .id(BIKE_ENTITY.getId())
                .type(ELECTRIC)
                .status(INACTIVE)
                .kms(1234)
                .lastMaintenanceDate(BIKE_ENTITY.getLastMaintenanceDate())
                .build();

        assertThat(updatedBike).isEqualTo(expectedBike);
    }

    @Test
    void givenThatBikeDoesNotExist_whenUpdatingStation_thenBikeNotFoundIsThrown() {
        // given
        when(bikeRepository.findById(1)).thenReturn(Optional.empty());

        // then
        final var exception = assertThrowsExactly(
                BikeNotFoundException.class,
                () -> bikeService.updateBike(1, BikeRequest.builder().build())
        );

        // then
        assertThat(exception.getReason())
                .isEqualTo("The requested bike does not exist, please verify that the identifier is correct.");
    }
}
