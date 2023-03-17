package com.playground.demo.controllers;

import com.playground.demo.models.StationModel;
import com.playground.demo.services.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<String> getStations() {
        return ResponseEntity.ok("{\"stations\" : []}");
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
    public ResponseEntity<String> createStation(@PathVariable int id) {
        return ResponseEntity.ok("{\"id\" : " + id + "}");
    }
}
