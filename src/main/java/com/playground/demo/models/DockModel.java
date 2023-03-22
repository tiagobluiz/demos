package com.playground.demo.models;

import com.playground.demo.persistence.entities.enums.AssetStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DockModel {
        private int id;
        private AssetStatus status;
        private BikeModel bike;
}
