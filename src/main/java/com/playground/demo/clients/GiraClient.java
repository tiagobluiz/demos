package com.playground.demo.clients;

import com.playground.demo.models.Station;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "giraClient", url = "${gira.api.url}", path = "/cycling")
public interface GiraClient {

    @RequestMapping(method = GET, value = "/gira/station/{stationId}")
    Station getStation(@PathVariable int stationId);
}
