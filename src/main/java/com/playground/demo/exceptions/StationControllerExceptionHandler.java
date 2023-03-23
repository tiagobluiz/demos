package com.playground.demo.exceptions;

import com.playground.demo.controllers.StationController;
import com.playground.demo.exceptions.notfound.StationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = StationController.class)
public class StationControllerExceptionHandler {

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ExceptionalResponse> handleStationNotFound(final StationNotFoundException notFoundException) {
        final var body = ExceptionalResponse.builder()
                .reason(notFoundException.getReason())
                .faultyValue("id", notFoundException.getStationId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
