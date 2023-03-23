package com.playground.demo.exceptions.notfound;

import com.playground.demo.exceptions.ApiException;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotFoundException extends ApiException {

    private String reason;

}
