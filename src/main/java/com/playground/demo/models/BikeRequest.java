package com.playground.demo.models;

import com.playground.demo.models.enums.AssetStatus;
import com.playground.demo.models.enums.BikeType;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.playground.demo.models.enums.AssetStatus.ACTIVE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BikeRequest {
    private BikeType type;
    @Builder.Default
    private AssetStatus status = ACTIVE;

    @PositiveOrZero
    private int kms;
}
