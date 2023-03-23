package com.playground.demo.exceptions;

import lombok.Builder;
import lombok.Singular;

import java.util.Map;

@Builder
public record ExceptionalResponse(
        String reason,
        @Singular Map<String, Object> faultyValues
) {
}
