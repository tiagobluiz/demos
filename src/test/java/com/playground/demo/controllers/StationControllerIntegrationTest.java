package com.playground.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.demo.exceptions.ExceptionalResponse;
import com.playground.demo.models.NearStationsModel;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.enums.StationStatus;
import com.playground.demo.utils.PostgisSQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Arrays;

import static com.playground.demo.utils.TestUtils.readFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgisSQLContainerInitializer.class})
@ActiveProfiles("test")
class StationControllerIntegrationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenCoordinatesAndRadius_whenGettingStationsWithinRadius_thenStationWithinRadiusAreReturned() throws IOException {
        // given
        final var uri = UriComponentsBuilder.newInstance()
                .path("/stations")
                .queryParam("longitude", 38.774393608389396)
                .queryParam("latitude", -9.158491534606988)
                .queryParam("radius", 2000)
                .queryParam("statuses", Arrays.asList(StationStatus.values()))
                .build()
                .toUriString();

        // when
        final var response = restTemplate.getForEntity(uri, NearStationsModel.class);

        // then
        final var expectedResponse = MAPPER.readValue(
                readFileAsString(this.getClass(), "stationsController_getStations_success.json"),
                NearStationsModel.class
        );

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenCoordinatesAndRadiusForNonCoveredPoint_whenGettingStationWithinRadius_thenNoStationsAreReturned() {
        // given
        final var uri = UriComponentsBuilder.newInstance()
                .path("/stations")
                .queryParam("longitude", 0)
                .queryParam("latitude", 0)
                .queryParam("radius", 2000)
                .build()
                .toUriString();

        // when
        final var response = restTemplate.getForEntity(uri, NearStationsModel.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(NearStationsModel.builder().build());
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenNoQueryParameters_whenGettingStations_thenAllActiveStationAreReturned() throws IOException {
        // when
        final var response = restTemplate.getForEntity("/stations", NearStationsModel.class);

        // then
        final var expectedResponse = MAPPER.readValue(
                readFileAsString(this.getClass(), "stationsController_getActiveStations_success.json"),
                NearStationsModel.class
        );

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenAStationId_whenGettingAStation_thenStationDataIsReturned() throws IOException {
        // when
        final var response = restTemplate.getForEntity("/stations/1", StationModel.class);

        // then
        final var expectedResponse = MAPPER.readValue(
                readFileAsString(this.getClass(), "stationsController_getSingleStation_success.json"),
                StationModel.class
        );

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenAnStationId_whenGettingAStationThatDoesNotExist_thenNotFoundIsReturned() {
        // when
        final var response = restTemplate.getForEntity("/stations/999", ExceptionalResponse.class);

        // then
        final var expectedResponse = ExceptionalResponse.builder()
                .reason("The requested station does not exist, please verify that the identifier is correct.")
                .faultyValue("id", 999)
                .build();

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @ParameterizedTest
    @ValueSource(strings = {
            "stationsController_createStation_invalidDuplicatedCoordinates.json",
            "stationsController_createStation_invalidParish.json",
            "stationsController_createStation_invalidStatus.json"
    })
    void givenInvalidArguments_whenCreatingAStation_thenBadRequestIsReturned(String fileName) throws IOException {
        // given
        final String input = readFileAsString(this.getClass(), fileName);

        // when
        final ResponseEntity<String> response = restTemplate.postForEntity("/stations", input, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenValidArguments_whenCreationAStation_thenStationIsPersisted() throws IOException {
        // given
        final String input = readFileAsString(this.getClass(), "stationsController_createStation_validRequest.json");

        // when
        final ResponseEntity<StationModel> creationResponse = restTemplate.postForEntity("/stations", input, StationModel.class);

        // then
        assertThat(creationResponse.getStatusCode()).isEqualTo(CREATED);

        final StationModel creationBody = creationResponse.getBody();
        assertThat(creationBody)
                .isNotNull();
        // todo remaingn data;

        // Verify that it was persisted
        final ResponseEntity<StationModel> response = restTemplate.getForEntity("/stations/" + creationBody.getId(), StationModel.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .isNotNull();
        // todo remaining data;
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenValidArguments_whenUpdatingAStation_thenUpdateIsPersisted() throws IOException {
        // given
        final String input = readFileAsString(this.getClass(), "stationsController_updateStation_validRequest.json");
        final HttpEntity<String> httpEntity = new HttpEntity<>(input);

        // when
        final ResponseEntity<StationModel> creationResponse = restTemplate.exchange("/stations/2", PUT, httpEntity, StationModel.class);

        // then
        assertThat(creationResponse.getStatusCode()).isEqualTo(OK);
        assertThat(creationResponse.getBody())
                .isNotNull();
        // todo remaining data

        // Verify that it was persisted
        final ResponseEntity<StationModel> response = restTemplate.getForEntity("/stations/2", StationModel.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .isNotNull();
        // todo remaining data
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenNonexistentStation_whenUpdatingAStation_thenNotFoundIsReturned() {
        // when
        final ResponseEntity<String> creationResponse = restTemplate.exchange("/stations/999", PUT, HttpEntity.EMPTY, String.class);

        // then
        assertThat(creationResponse.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @ParameterizedTest
    @ValueSource(strings = {
            "stationsController_updateStation_invalidDuplicatedCoordinates.json",
            "stationsController_updateStation_invalidParish.json",
            "stationsController_updateStation_invalidStatus.json"
    })
    void givenInvalidArguments_whenUpdatingAStation_thenBadRequestIsReturned(String fileName) throws IOException {
        // given
        final String input = readFileAsString(this.getClass(), fileName);
        final HttpEntity<String> httpEntity = new HttpEntity<>(input);

        // when
        final ResponseEntity<String> response = restTemplate.exchange("/stations/1", PUT, httpEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}