package org.shop.traffic.controller;

import lombok.RequiredArgsConstructor;
import org.shop.traffic.dto.request.CreateIntersectionRequest;
import org.shop.traffic.dto.request.VehicleRequest;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.dto.response.VehicleResponse;
import org.shop.traffic.service.IntersectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/intersection")
@RequiredArgsConstructor
public class IntersectionController {

    private final IntersectionService intersectionService;

    /** Handles POST requests to create a new traffic intersection **/
    @PostMapping
    public ResponseEntity<IntersectionResponse> createIntersection(
            @RequestBody CreateIntersectionRequest request) {
        IntersectionResponse response = intersectionService.createTrafficIntersection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /** Handles GET requests to retrieve the current state of a traffic intersection by its ID.**/
    @GetMapping("/{id}")
    public ResponseEntity<IntersectionResponse> getIntersection(@PathVariable UUID id){
        IntersectionResponse response = intersectionService.getTrafficIntersection(id);
        return ResponseEntity.ok(response);
    }
    /** Handles POST requests to add a vehicle to a specific intersection **/
    @PostMapping("/{id}/vehicle")
    public ResponseEntity<VehicleResponse> addVehicleToIntersection(
            @RequestBody VehicleRequest request, @PathVariable UUID id){
        VehicleResponse response = intersectionService.addVehicle(request, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
