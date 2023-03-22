package com.playground.demo.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Parish {
    LUMIAR("Lumiar"),
    BELEM("Belém"),
    BENFICA("Benfica"),
    PENHA_DE_FRANCA("Penha de França");

    @Getter
    private final String text;
}
