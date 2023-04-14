package com.playground.demo.persistence.repositories;

import com.playground.demo.persistence.entities.BikeEntity;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.entities.enums.BikeType;
import com.playground.demo.utils.PostgisSQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgisSQLContainerInitializer.class})
@ActiveProfiles("test")
class BikeRepositoryTest {

    // Reoccurring data
    private static final BikeEntity BIKE_1 = BikeEntity.builder()
            .id(111)
            .type(BikeType.ELECTRIC)
            .status(AssetStatus.ACTIVE)
            .km(555)
            .lastMaintenanceDate(LocalDate.of(2022, 3, 15))
            .build();
    private static final BikeEntity BIKE_2 = BikeEntity.builder()
            .id(222)
            .type(BikeType.ELECTRIC)
            .status(AssetStatus.ACTIVE)
            .km(777)
            .lastMaintenanceDate(LocalDate.of(2022, 3, 14))
            .build();

    private static final BikeEntity BIKE_3 = BikeEntity.builder()
            .id(333)
            .type(BikeType.CLASSIC)
            .status(AssetStatus.REQUIRING_MAINTENANCE)
            .km(1234)
            .lastMaintenanceDate(LocalDate.of(2021, 3, 15))
            .build();

    @Autowired
    private BikeRepository bikeRepository;

    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenAType_whenGettingBikesByType_thenBikesOfThatTypeAreReturned() {
        // given
        final var expectedBikes = List.of(BIKE_1, BIKE_2);

        // when
        final var bikes = bikeRepository.findByType(BikeType.ELECTRIC);

        // then
        assertThat(bikes)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("dock") // todo: remove once docks are implemented
                .containsExactlyElementsOf(expectedBikes);
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenAStatus_whenGettingBikesByStatus_thenBikesOfThatStatusAreReturned() {
        // given
        final var expectedBikes = List.of(BIKE_3);

        // when
        final var bikes = bikeRepository.findByStatus(AssetStatus.REQUIRING_MAINTENANCE);

        // then
        assertThat(bikes)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("dock") // todo: remove once docks are implemented
                .containsExactlyElementsOf(expectedBikes);
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenADate_whenGettingBikesWithLastMaintenanceBefore_thenBikesWhoseMaintenanceDateIsLowerAreReturned() {
        // given
        final var expectedBikes = List.of(BIKE_2, BIKE_3);

        // when
        final var bikes = bikeRepository.findByLastMaintenanceDateBeforeOrEqual(BIKE_2.getLastMaintenanceDate());

        // then
        assertThat(bikes)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("dock") // todo: remove once docks are implemented
                .containsExactlyElementsOf(expectedBikes);
    }
}
