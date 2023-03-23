package com.playground.demo.controllers;

import com.playground.demo.models.CreateStationRequest;
import com.playground.demo.models.NearStationsModel;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.StationsSearchCriteria;
import com.playground.demo.services.StationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<StationModel> createStation(@RequestBody final CreateStationRequest createRequest) {
        final var createdStation = stationService.createStation(createRequest);

        final var uri = UriComponentsBuilder
                .fromPath("/stations/{id}")
                .buildAndExpand(createdStation.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdStation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStation(@PathVariable int id, StationModel station) {
        return ResponseEntity.ok("{\"id\" : " + id + "}");
    }
}
