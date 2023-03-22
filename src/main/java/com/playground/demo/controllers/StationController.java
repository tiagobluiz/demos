package com.playground.demo.controllers;

import com.playground.demo.models.NearStationsModel;
import com.playground.demo.models.StationModel;
import com.playground.demo.models.StationsSearchCriteria;
import com.playground.demo.services.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<NearStationsModel> getStations(@Valid StationsSearchCriteria searchCriteria) {
        final var nearStations = stationService.getAllStations(searchCriteria);

        return ResponseEntity.ok(nearStations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getSingleStation(@PathVariable int id) {
        return ResponseEntity.ok("{\"id\" : " + id + "}");
    }

    @PostMapping
    public ResponseEntity<String> createStation(StationModel station) {
        return ResponseEntity.ok("{\"id\" : " + 1 + "}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStation(@PathVariable int id, StationModel station) {
        return ResponseEntity.ok("{\"id\" : " + id + "}");
    }
}
