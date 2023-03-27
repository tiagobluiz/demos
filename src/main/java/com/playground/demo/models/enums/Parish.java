package com.playground.demo.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Parish {
    LUMIAR("Lumiar"),
    BELEM("Belém"),
    BENFICA("Benfica"),
    PENHA_DE_FRANCA("Penha de França");

    @JsonProperty
    private final String text;
}
