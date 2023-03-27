package com.playground.demo.exceptions;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionalResponse {
    private String reason;
    @Singular
    private Map<String, Object> faultyValues;
}
