package com.playground.demo.exceptions.notfound;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;


@RequiredArgsConstructor
public class StationNotFoundException extends NotFoundException {

    @Getter
    private final String reason = "The requested station does not exist, please verify that the identifier is correct.";

    private final int stationId;

    public Map<String, Object> getFaultyValues() {
        return Map.of("id", stationId);
    }
}
