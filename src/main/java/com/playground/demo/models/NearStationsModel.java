package com.playground.demo.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearStationsModel {

    @Builder.Default
    List<StationModel> stations = new ArrayList<>();
}
