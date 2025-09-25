package org.shop.traffic.controller;

import lombok.RequiredArgsConstructor;
import org.shop.traffic.dto.request.*;
import org.shop.traffic.dto.response.CycleResponse;
import org.shop.traffic.dto.response.SimulationResponse;
import org.shop.traffic.service.TrafficSimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/traffic-simulation")
@RequiredArgsConstructor
public class TrafficSimulationController {

    private final TrafficSimulationService trafficSimulationService;

    /** Runs a full simulation based on a JSON file with commands. **/
    @PostMapping("/run")
    public ResponseEntity<SimulationResponse> runSimulation(
            @RequestBody SimulationRequest request){
        SimulationResponse response = trafficSimulationService.runSimulation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Handles a request to generate a new traffic light cycle based on the provided input. **/
    @PostMapping("/cycle")
    public ResponseEntity<CycleResponse> updateCycle(
            @RequestBody CycleRequest request, @RequestBody LaneRequest laneRequest){
        CycleResponse response = trafficSimulationService.updateTrafficLightsCycle(request, laneRequest.laneData());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
