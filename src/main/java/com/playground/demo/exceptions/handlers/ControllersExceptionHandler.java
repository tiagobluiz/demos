package com.playground.demo.exceptions.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.playground.demo.exceptions.ExceptionalResponse;
import com.playground.demo.exceptions.notfound.BikeNotFoundException;
import com.playground.demo.exceptions.notfound.StationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(basePackages = "com.playground.demo.controllers")
public class ControllersExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionalResponse> handleNonCoveredExceptions(final Exception exception) {
        log.error("A non-covered exception occurred", exception);

        final var body = ExceptionalResponse.builder()
                .reason("An unexpected exception occurred in the system, please contact an administrator if this persists.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionalResponse> handleMessageNotReadable(final HttpMessageNotReadableException messageNotReadableException) {
        var cause = (JsonMappingException) messageNotReadableException.getCause();

        var faultyField = cause.getPath().stream()
                .map(path -> path.getFieldName() != null ? path.getFieldName() : "")
                .collect(Collectors.joining("."));

        final var message = cause.getCause() != null ? cause.getCause().getMessage() : cause.getMessage();

        log.error("The received request is badly formed. Message = {}", message);

        final var body = ExceptionalResponse.builder()
                .reason("The request was badly formed! Please verify that the indicated field is using the accepted values.")
                .faultyValue(faultyField, "Not valid")
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionalResponse> handleBindException(final BindException bindException) {
        log.error("The received request has an invalid argument in the payload. Message = {}", bindException.getMessage());

        final var bodyBuilder = ExceptionalResponse.builder()
                .reason("The request was badly formed! Please verify that the indicated field is using the accepted values.");

        bindException.getFieldErrors().forEach(error -> bodyBuilder.faultyValue(error.getField(), error.getRejectedValue()));

        return ResponseEntity.badRequest().body(bodyBuilder.build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionalResponse> handleDataIntegrityViolation(final DataIntegrityViolationException integrityViolationException) {
        log.error("The received request led into a data integrity violation. Message = {}", integrityViolationException.getMessage());

        final var body = ExceptionalResponse.builder()
                .reason("The request was badly formed! Please verify that a station with the given values does not exist already.")
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ExceptionalResponse> handleStationNotFound(final StationNotFoundException notFoundException) {
        log.error("Station with id {} could not be found", notFoundException.getStationId());

        final var body = ExceptionalResponse.builder()
                .reason(notFoundException.getReason())
                .faultyValue("id", notFoundException.getStationId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BikeNotFoundException.class)
    public ResponseEntity<ExceptionalResponse> handleStationNotFound(final BikeNotFoundException notFoundException) {
        log.error("Bike with id {} could not be found", notFoundException.getBikeId());

        final var body = ExceptionalResponse.builder()
                .reason(notFoundException.getReason())
                .faultyValue("id", notFoundException.getBikeId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
