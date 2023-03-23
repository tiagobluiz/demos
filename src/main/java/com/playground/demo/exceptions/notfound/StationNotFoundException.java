package com.playground.demo.exceptions.notfound;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class StationNotFoundException extends NotFoundException {

    private final String reason = "The requested station does not exist, please verify that the identifier is correct.";

    private final int stationId;
}
