package org.shop.traffic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shop.traffic.dto.request.CreateIntersectionRequest;
import org.shop.traffic.dto.request.VehicleRequest;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.dto.response.VehicleResponse;
import org.shop.traffic.mapper.IntersectionMapper;
import org.shop.traffic.mapper.VehicleMapper;
import org.shop.traffic.model.Intersection;
import org.shop.traffic.model.Vehicle;
import org.shop.traffic.repository.IntersectionRepository;
import org.shop.traffic.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrafficSimulationService {

    private final IntersectionRepository intersectionRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final IntersectionMapper intersectionMapper;

    /** Adding a vehicle to a specific road **/
    @Transactional
    public VehicleResponse addVehicle(VehicleRequest request, UUID id){
        var intersection = intersectionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Intersection with id: " + id + "not found."));

        var entity = Vehicle.builder()
                .id(intersection.getId())
                .vehicleType(request.vehicleType())
                .speed(request.speed())
                .length(request.length())
                .arrivalTime(request.arrivalTime())
                .priority(request.priority())
                .turn(request.intendedTurn())
                .build();

        var saved = vehicleRepository.save(entity);
        return vehicleMapper.toResponse(saved);
    }

    /**
     * Creates a new intersection based on the request data and returns its DTO representation.
     */
    @Transactional
    public IntersectionResponse createTrafficIntersection(CreateIntersectionRequest request){

        var entity = Intersection.builder()
                .name(request.name())
                .location(request.location())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();
        var saved = intersectionRepository.save(entity);
        return intersectionMapper.toResponse(saved);
    }

    /**
     * Retrieves an existing traffic intersection by its ID and maps it to a response DTO.
     */
    public IntersectionResponse getTrafficIntersection(UUID id){
        var entity = intersectionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Intersection with id: " + id + "not found."));
        return intersectionMapper.toResponse(entity);
    }
}
