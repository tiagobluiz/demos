package com.playground.demo.controllers;

import com.playground.demo.models.NearStationsModel;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.StationRequest;
import com.playground.demo.models.StationsSearchCriteria;
import com.playground.demo.services.StationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<NearStationsModel> getStations(@Valid final StationsSearchCriteria searchCriteria) {
        final var nearStations = stationService.getAllStations(searchCriteria);

        return ResponseEntity.ok(nearStations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationModel> getSingleStation(@PathVariable final int id) {
        final var station = stationService.getStation(id);

        return ResponseEntity.ok(station);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StationModel> createStation(@RequestBody @NotNull @Valid final StationRequest createRequest) {
        final var createdStation = stationService.createStation(createRequest);

        final var uri = UriComponentsBuilder
                .fromPath("/stations/{id}")
                .buildAndExpand(createdStation.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdStation);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StationModel> updateStation(@PathVariable int id, @RequestBody @NotNull @Valid final StationRequest updateRequest) {
        final var updatedStation = stationService.updateStation(id, updateRequest);

        return ResponseEntity.ok(updatedStation);
    }
}
