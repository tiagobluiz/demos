package com.playground.demo.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BikeType {
    ELECTRIC("Electric"),
    CLASSIC("Classic");

    @Getter
    private final String text;
}
