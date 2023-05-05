package com.playground.demo.controllers;

import com.playground.demo.models.BikeModel;
import com.playground.demo.models.BikeRequest;
import com.playground.demo.services.BikeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/bikes")
@RequiredArgsConstructor
public class BikeController {

    private final BikeService bikeService;

    @GetMapping("/{id}")
    public ResponseEntity<BikeModel> getSingleBike(@PathVariable final int id) {
        final var bike = bikeService.getBike(id);

        return ResponseEntity.ok(bike);
    }

    @PostMapping
    public ResponseEntity<BikeModel> createBike(@RequestBody @NotNull @Valid final BikeRequest createRequest) {
        final var createdBike = bikeService.createBike(createRequest);

        final var uri = UriComponentsBuilder
                .fromPath("/bikes/{id}")
                .buildAndExpand(createdBike.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdBike);
    }
}
