package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Parish {
    LUMIAR("Lumiar"),
    BELEM("Belém"),
    BENFICA("Benfica"),
    PENHA_DE_FRANCA("Penha de França");

    @JsonValue
    private final String text;
}
