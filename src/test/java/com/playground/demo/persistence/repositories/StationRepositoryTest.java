package com.playground.demo.persistence.repositories;

import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.persistence.entities.enums.Parish;
import com.playground.demo.persistence.entities.enums.StationStatus;
import com.playground.demo.utils.PostgisSQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgisSQLContainerInitializer.class})
@ActiveProfiles("test")
class StationRepositoryTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired
    private StationRepository stationRepository;

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenStationsWithinRadius_whenGettingStationsWithin50Meters_thenStationsInRadiusAreReturned() {
        // given
        final var coordinates = GEOMETRY_FACTORY.createPoint(new Coordinate(38.77066006800165, -9.160284356927665));

        // when
        final var stations = stationRepository.findAllInRadius(coordinates, 2000);

        // then
        assertThat(stations)
                .isNotNull()
                .isNotEmpty()
                .extracting(StationEntity::getId)
                .containsOnly(1, 2);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenNoStationsWithinRadius_whenGettingStationsWithin50Meters_thenEmptyListReturned() {
        // given
        final var coordinates = GEOMETRY_FACTORY.createPoint(new Coordinate(0, 0));

        // when
        final var stations = stationRepository.findAllInRadius(coordinates, 2000);

        // then
        assertThat(stations)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void givenValidArguments_whenCreatingStation_entityIsPersisted() {
        // given
        final var coordinates = GEOMETRY_FACTORY.createPoint(new Coordinate(38.772483954508274, -9.154035912185192));
        final var station = StationEntity.builder()
                .parish(Parish.LUMIAR)
                .address("Rua Nóbrega e Sousa")
                .status(StationStatus.PLANNED)
                .coordinates(coordinates)
                .build();

        // when
        final var savedStation = stationRepository.save(station);

        // then
        assertThat(savedStation)
                .isNotNull()
                .extracting(StationEntity::getId)
                .isNotNull();
        assertThat(savedStation)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(station);

    }
}