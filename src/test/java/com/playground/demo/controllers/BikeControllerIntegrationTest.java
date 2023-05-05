package com.playground.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.demo.exceptions.ExceptionalResponse;
import com.playground.demo.models.BikeModel;
import com.playground.demo.models.BikeRequest;
import com.playground.demo.models.enums.AssetStatus;
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
import java.time.LocalDate;

import static com.playground.demo.models.enums.AssetStatus.ACTIVE;
import static com.playground.demo.models.enums.AssetStatus.INACTIVE;
import static com.playground.demo.models.enums.BikeType.CLASSIC;
import static com.playground.demo.models.enums.BikeType.ELECTRIC;
import static com.playground.demo.utils.TestUtils.readFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgisSQLContainerInitializer.class})
@ActiveProfiles("test")
class BikeControllerIntegrationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenABikeId_whenGettingABike_thenBikeIsReturned() {
        // when
        final var response = restTemplate.getForEntity("/bikes/111", BikeModel.class);

        // then
        assertThat(response).isNotNull();

        final var expectedResponse = BikeModel.builder()
                .id(111)
                .type(ELECTRIC)
                .status(ACTIVE)
                .kms(555)
                .lastMaintenanceDate(LocalDate.of(2022, 3, 15))
                .build();

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenABikeId_whenGettingABikeThatDoesNotExist_thenNotFoundIsReturned() {
        // when
        final var response = restTemplate.getForEntity("/bikes/987654", BikeModel.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void givenValidArguments_whenCreatingABike_thenBikeIsPersisted() {
        // given
        final var bikeRequest = BikeRequest.builder()
                .type(ELECTRIC)
                .status(AssetStatus.INACTIVE)
                .kms(123456798) // should be ignored as all bikes start with 0
                .build();

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(bikeRequest, headers);

        // when
        final var response = restTemplate.postForEntity("/bikes", entity, BikeModel.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        final var createdBike = response.getBody();

        assertThat(createdBike).isNotNull();
        assertThat(createdBike.getId()).isNotZero();
        assertThat(createdBike.getKms()).isZero();

        assertThat(response.getHeaders().getLocation())
                .isNotNull()
                .isEqualTo(UriComponentsBuilder.fromPath("/bikes/{id}").buildAndExpand(createdBike.getId()).toUri());

        // Verify it was persisted
        final var getBikeResponse = restTemplate.getForEntity("/bikes/" + createdBike.getId(), BikeModel.class);

        assertThat(getBikeResponse.getStatusCode()).isEqualTo(OK);
        assertThat(getBikeResponse.getBody()).isEqualTo(createdBike);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "bikeController/bikeRequest_invalidStatus.json",
            "bikeController/bikeRequest_invalidType.json"
    })
    void givenInvalidArguments_whenCreatingABike_thenBadRequestIsReturned(final String filename) throws IOException {
        // given
        final var input = readFileAsString(this.getClass(), filename);

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var response = restTemplate.postForEntity("/bikes", entity, ExceptionalResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody())
                .isNotNull()
                .extracting(ExceptionalResponse::getReason)
                .isEqualTo("The request was badly formed! Please verify that the indicated field is using the accepted values.");
    }

    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/db/init.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/db/clean.sql")
    @Test
    void givenValidArguments_whenUpdatingABike_thenUpdatedBikeIsPersisted() {
        // given
        final var input = BikeRequest.builder()
                .type(CLASSIC)
                .status(INACTIVE)
                .kms(123456)
                .build();

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var updateResponse = restTemplate.exchange("/bikes/111", PUT, entity, BikeModel.class);

        // then
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(OK);
        assertThat(updateResponse.getBody()).isNotNull();

        final var updatedBike = updateResponse.getBody();

        assertThat(updatedBike)
                .extracting(BikeModel::getId, BikeModel::getType, BikeModel::getStatus, BikeModel::getKms)
                .containsExactly(111, input.getType(), input.getStatus(), input.getKms());

        // verify it was persisted
        final var response = restTemplate.getForEntity("/bikes/111", BikeModel.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(updatedBike);
    }

    @Test
    void givenNonexistentBike_whenUpdatingABike_thenNotFoundIsReturned() {
        // given
        final var input = BikeRequest.builder()
                .type(ELECTRIC)
                .status(ACTIVE)
                .kms(123456)
                .build();

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var response = restTemplate.exchange("/bikes/999", PUT, entity, ExceptionalResponse.class);

        // then
        final var expectedResponse = ExceptionalResponse.builder()
                .reason("The requested bike does not exist, please verify that the identifier is correct.")
                .faultyValue("id", 999)
                .build();

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "bikeController/bikeRequest_invalidStatus.json",
            "bikeController/bikeRequest_invalidType.json",
            "bikeController/bikeRequest_invalidKms.json"
    })
    void givenInvalidArguments_whenUpdatingABike_thenBadRequestIsReturned(final String filename) throws IOException {
        // given
        final var input = readFileAsString(this.getClass(), filename);

        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        final var entity = new HttpEntity<>(input, headers);

        // when
        final var response = restTemplate.exchange("/bikes/111", PUT, entity, ExceptionalResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody())
                .isNotNull()
                .extracting(ExceptionalResponse::getReason)
                .isEqualTo("The request was badly formed! Please verify that the indicated field is using the accepted values.");
    }
}
