package com.playground.demo.services;

import com.playground.demo.clients.GiraClient;
import com.playground.demo.models.Station;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    private final GiraClient giraClient;

    public StationService(GiraClient giraClient) {
        this.giraClient = giraClient;
    }

    public Station getStation(int id) {
        return giraClient.getStation(id);
    }
}
