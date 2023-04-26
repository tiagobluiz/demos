package com.playground.demo.exceptions.notfound;

import com.playground.demo.exceptions.ApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BikeNotFoundException extends ApiException {

    private final String reason = "The requested bike does not exist, please verify that the identifier is correct.";

    private final int bikeId;
}
