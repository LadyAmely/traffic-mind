package org.shop.traffic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shop.traffic.dto.request.CreateIntersectionRequest;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.model.Intersection;
import org.shop.traffic.repository.IntersectionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrafficSimulationService {

    private final IntersectionRepository intersectionRepository;

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
        return toResponse(saved);
    }

    /**
     * Retrieves an existing traffic intersection by its ID and maps it to a response DTO.
     */
    public IntersectionResponse getTrafficIntersection(UUID id){
        var entity = intersectionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Intersection with id: " + id + "not found."));
        return toResponse(entity);
    }

    /**
     * Maps an Intersection entity to its corresponding IntersectionResponse DTO.
     */
    private IntersectionResponse toResponse(
            Intersection intersection
    ){
        return new IntersectionResponse(
          intersection.getId(),
          intersection.getName(),
          intersection.getLocation(),
          intersection.getLatitude(),
          intersection.getLongitude()
        );
    }
}
