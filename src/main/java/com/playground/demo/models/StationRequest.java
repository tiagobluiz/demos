package com.playground.demo.models;

import com.playground.demo.models.enums.Parish;
import com.playground.demo.models.enums.StationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StationRequest {
    private double longitude;
    private double latitude;
    @NotBlank
    private String address;
    private Parish parish;
    private StationStatus status;
    @PositiveOrZero
    private int docks;
}
