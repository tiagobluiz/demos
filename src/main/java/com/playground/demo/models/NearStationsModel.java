package com.playground.demo.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearStationsModel {
    List<StationModel> stations;
}
