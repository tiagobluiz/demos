package com.playground.demo.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationDocks {

    @Builder.Default
    private List<DockModel> docks = new ArrayList<>();
}
