package com.playground.demo.exceptions.notfound;

import com.playground.demo.exceptions.ApiException;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotFoundException extends ApiException {

    private final int status = HttpStatus.NOT_FOUND.value();

    private String reason;

    private Map<String, Object> faultyValues;
}
