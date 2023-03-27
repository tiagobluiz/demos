package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BikeType {
    ELECTRIC("Electric"),
    CLASSIC("Classic");

    @JsonProperty
    private final String text;
}
