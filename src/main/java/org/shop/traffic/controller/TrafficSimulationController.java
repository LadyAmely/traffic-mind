package org.shop.traffic.controller;

import lombok.RequiredArgsConstructor;
import org.shop.traffic.dto.request.CreateIntersectionRequest;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.service.TrafficSimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/traffic-simulation")
@RequiredArgsConstructor
public class TrafficSimulationController {

    private final TrafficSimulationService trafficSimulationService;

    /** Handles POST requests to create a new traffic intersection **/
    @PostMapping("/intersection")
    public ResponseEntity<IntersectionResponse> createIntersection(
           @RequestBody CreateIntersectionRequest request) {
        IntersectionResponse response = trafficSimulationService.createTrafficIntersection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Handles GET requests to retrieve the current state of a traffic intersection by its ID.
     */
    @GetMapping("/intersection/{id}")
    public ResponseEntity<IntersectionResponse> getIntersection(@PathVariable UUID id){
        IntersectionResponse response = trafficSimulationService.getTrafficIntersection(id);
        return ResponseEntity.ok(response);
    }
}
