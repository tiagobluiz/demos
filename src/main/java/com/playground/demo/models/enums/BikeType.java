package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BikeType {
    ELECTRIC("Electric"),
    CLASSIC("Classic");

    @JsonValue
    private final String text;
}
