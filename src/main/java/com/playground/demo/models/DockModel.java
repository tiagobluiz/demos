package com.playground.demo.models;

import com.playground.demo.persistence.entities.enums.AssetStatus;
import jakarta.annotation.Nullable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DockModel {
        private int id;
        private int number;
        private AssetStatus status;
        @Nullable
        private String bikeDesignation;
}
