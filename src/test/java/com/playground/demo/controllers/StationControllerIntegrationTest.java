package com.playground.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.demo.exceptions.ExceptionalResponse;
import com.playground.demo.models.NearStationsModel;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.StationRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Arrays;

import static com.playground.demo.models.enums.Parish.LUMIAR;
import static com.playground.demo.models.enums.StationStatus.INSTALLING;
import static com.playground.demo.models.enums.StationStatus.TESTING;
import static com.playground.demo.utils.TestUtils.readFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @ParameterizedTest
    @ValueSource(strings = {
            "stationsController_stationRequest_invalidParish.json",
            "stationsController_stationRequest_invalidStatus.json",
            "stationsController_stationRequest_invalidDocks.json",
            "stationsController_stationRequest_invalidAddress.json"
    })
    void givenInvalidArguments_whenCreatingAStation_thenBadRequestIsReturned(String fileName) throws IOException {
        // given
        final var input = readFileAsString(this.getClass(), fileName);

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var response = restTemplate.postForEntity("/stations", entity, ExceptionalResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody())
                .isNotNull()
                .extracting(ExceptionalResponse::getReason)
                .isIn(
                        "The request was badly formed! Please verify that the indicated field is using the accepted values.",
                        "The request was badly formed! Please verify that a station with the given values does not exist already."
                );
    }

    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenValidArguments_whenCreatingAStation_thenStationIsPersisted() {
        // given
        final var input = StationRequest.builder()
                .longitude(38.773493797784944)
                .latitude(-9.161430406823348)
                .address("Estrada da Torre")
                .parish(LUMIAR)
                .status(INSTALLING)
                .docks(2)
                .build();

        // when
        final var creationResponse = restTemplate.postForEntity("/stations", input, StationModel.class);

        // then
        assertThat(creationResponse.getStatusCode()).isEqualTo(CREATED);

        final var createdStation = creationResponse.getBody();

        assertThat(createdStation)
                .isNotNull()
                .extracting(StationModel::getLongitude, StationModel::getLatitude, StationModel::getParish, StationModel::getStatus, StationModel::getTotalDocks)
                .containsExactly(input.getLongitude(), input.getLatitude(), input.getParish(), input.getStatus(), input.getDocks());

        assertThat(createdStation.getId()).isNotZero();

        assertThat(creationResponse.getHeaders().getLocation())
                .isNotNull()
                .isEqualTo(UriComponentsBuilder.fromPath("/stations/{id}").buildAndExpand(createdStation.getId()).toUri());

        // Verify that it was persisted
        final var stationResponse = restTemplate.getForEntity("/stations/" + createdStation.getId(), StationModel.class);

        assertThat(stationResponse.getStatusCode()).isEqualTo(OK);
        assertThat(stationResponse.getBody())
                .isNotNull()
                .isEqualTo(createdStation);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenValidArguments_whenUpdatingAStation_thenUpdateIsPersisted() {
        // given
        final var input = StationRequest.builder()
                .longitude(38.772483954508274)
                .latitude(-9.154035912185192)
                .address("Rua Nóbrega e Sousa")
                .parish(LUMIAR)
                .status(TESTING)
                .build();

        final var httpEntity = new HttpEntity<>(input);

        // when
        final var updateResponse = restTemplate.exchange("/stations/2", PUT, httpEntity, StationModel.class);

        // then
        final var updatedStation = updateResponse.getBody();

        assertThat(updateResponse.getStatusCode()).isEqualTo(OK);
        assertThat(updatedStation)
                .isNotNull()
                .extracting(StationModel::getLongitude, StationModel::getLatitude, StationModel::getParish, StationModel::getStatus)
                .containsExactly(input.getLongitude(), input.getLatitude(), input.getParish(), input.getStatus());

        // Verify that it was persisted
        final var response = restTemplate.getForEntity("/stations/2", StationModel.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(updatedStation);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenNonexistentStation_whenUpdatingAStation_thenNotFoundIsReturned() {
        // given
        final var input = StationRequest.builder()
                .longitude(38.772483954508274)
                .latitude(-9.154035912185192)
                .address("Rua Nóbrega e Sousa")
                .parish(LUMIAR)
                .status(TESTING)
                .build();

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var updateResponse = restTemplate.exchange("/stations/999", PUT, entity, ExceptionalResponse.class);

        // then
        final var expectedResponse = ExceptionalResponse.builder()
                .reason("The requested station does not exist, please verify that the identifier is correct.")
                .faultyValue("id", 999)
                .build();

        assertThat(updateResponse.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(updateResponse.getBody())
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Sql(scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @ParameterizedTest
    @ValueSource(strings = {
            "stationsController_stationRequest_invalidDuplicatedCoordinates.json",
            "stationsController_stationRequest_invalidParish.json",
            "stationsController_stationRequest_invalidStatus.json",
            "stationsController_stationRequest_invalidDocks.json",
            "stationsController_stationRequest_invalidAddress.json"
    })
    void givenInvalidArguments_whenUpdatingAStation_thenBadRequestIsReturned(String fileName) throws IOException {
        // given
        final var input = readFileAsString(this.getClass(), fileName);

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var updateResponse = restTemplate.exchange("/stations/1", PUT, entity, ExceptionalResponse.class);


        // then
        assertThat(updateResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(updateResponse.getBody()).isNotNull()
                .extracting(ExceptionalResponse::getReason)
                .isIn(
                        "The request was badly formed! Please verify that the indicated field is using the accepted values.",
                        "The request was badly formed! Please verify that a station with the given values does not exist already."
                );
    }
}