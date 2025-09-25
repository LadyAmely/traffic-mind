package org.shop.traffic.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.traffic.dto.request.CreateIntersectionRequest;
import org.shop.traffic.dto.request.VehicleRequest;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.dto.response.VehicleResponse;
import org.shop.traffic.mapper.IntersectionMapper;
import org.shop.traffic.mapper.VehicleMapper;
import org.shop.traffic.model.Intersection;
import org.shop.traffic.model.Turn;
import org.shop.traffic.model.Vehicle;
import org.shop.traffic.model.VehicleType;
import org.shop.traffic.repository.IntersectionRepository;
import org.shop.traffic.repository.VehicleRepository;
import org.shop.traffic.service.IntersectionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntersectionServiceTest {

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String NAME = "Liberty Circle";
    private static final String LOCATION = "Denver";
    private static final BigDecimal LATITUDE = BigDecimal.valueOf(39.7392);
    private static final BigDecimal LONGITUDE = BigDecimal.valueOf(-104.9903);
    private static final String VEHICLE_TYPE = "CAR";
    private static final float SPEED = 45;
    private static final float LENGTH = 4.5F;
    private static final int PRIORITY = 1;
    private static final String TURN = "LEFT";
    private static final LocalDateTime ARRIVAL_TIME = LocalDateTime.of(2025, 9, 25, 21, 0);

    @Mock
    private IntersectionRepository intersectionRepository;

    @Mock
    private IntersectionMapper intersectionMapper;

    @InjectMocks
    private IntersectionService intersectionService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Test
    @DisplayName("should create traffic intersection successfully")
    void shouldCreateTrafficIntersection() {
        // given
        CreateIntersectionRequest request = applyBaseData(CreateIntersectionRequest.builder()).build();
        Intersection savedEntity = applyBaseData(Intersection.builder().id(ID)).build();
        IntersectionResponse expectedResponse = applyBaseData(IntersectionResponse.builder().id(ID)).build();

        when(intersectionRepository.save(any(Intersection.class))).thenReturn(savedEntity);
        when(intersectionMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        // when
        IntersectionResponse result = intersectionService.createTrafficIntersection(request);

        // then
        assertEquals(expectedResponse, result);
        verify(intersectionRepository).save(any(Intersection.class));
        verify(intersectionMapper).toResponse(savedEntity);
    }

    @Test
    void shouldAddVehicleToExistingIntersection() {
        // given
        VehicleRequest request = buildVehicleRequest();
        Intersection intersection = buildIntersection();
        Vehicle vehicleEntity = buildVehicleEntity(request);
        Vehicle savedVehicle = vehicleEntity;
        VehicleResponse expectedResponse = buildVehicleResponse();

        when(intersectionRepository.findById(ID)).thenReturn(Optional.of(intersection));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);
        when(vehicleMapper.toResponse(savedVehicle)).thenReturn(expectedResponse);

        // when
        VehicleResponse result = intersectionService.addVehicle(request, ID);

        // then
        assertEquals(expectedResponse, result);
        verify(intersectionRepository).findById(ID);
        verify(vehicleRepository).save(any(Vehicle.class));
        verify(vehicleMapper).toResponse(savedVehicle);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenIntersectionDoesNotExist() {
        // given
        VehicleRequest request = buildVehicleRequest();

        when(intersectionRepository.findById(ID)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> intersectionService.addVehicle(request, ID));

        assertTrue(exception.getMessage().contains("Intersection with id: " + ID));
        verify(intersectionRepository).findById(ID);
        verifyNoInteractions(vehicleRepository, vehicleMapper);
    }

    private CreateIntersectionRequest.CreateIntersectionRequestBuilder applyBaseData(CreateIntersectionRequest.CreateIntersectionRequestBuilder builder) {
        return builder
                .name(NAME)
                .location(LOCATION)
                .latitude(LATITUDE)
                .longitude(LONGITUDE);
    }

    private Intersection.IntersectionBuilder applyBaseData(Intersection.IntersectionBuilder builder) {
        return builder
                .name(NAME)
                .location(LOCATION)
                .latitude(LATITUDE)
                .longitude(LONGITUDE);
    }

    private IntersectionResponse.IntersectionResponseBuilder applyBaseData(IntersectionResponse.IntersectionResponseBuilder builder) {
        return builder
                .name(NAME)
                .location(LOCATION)
                .latitude(LATITUDE)
                .longitude(LONGITUDE);
    }

    private VehicleRequest buildVehicleRequest() {
        return VehicleRequest.builder()
                .vehicleType(VehicleType.valueOf(VEHICLE_TYPE))
                .speed(SPEED)
                .length(LENGTH)
                .arrivalTime(ARRIVAL_TIME)
                .priority(PRIORITY)
                .intendedTurn(Turn.valueOf(TURN))
                .build();
    }

    private Intersection buildIntersection() {
        return Intersection.builder()
                .id(ID)
                .name(NAME)
                .location(LOCATION)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .build();
    }

    private Vehicle buildVehicleEntity(VehicleRequest request) {
        return Vehicle.builder()
                .id(ID)
                .vehicleType(request.vehicleType())
                .speed(request.speed())
                .length(request.length())
                .arrivalTime(request.arrivalTime())
                .priority(request.priority())
                .turn(request.intendedTurn())
                .build();
    }

    private VehicleResponse buildVehicleResponse() {
        return VehicleResponse.builder()
                .vehicleId(ID)
                .vehicleType(VehicleType.valueOf(VEHICLE_TYPE))
                .laneId(null)
                .arrivalTime(ARRIVAL_TIME)
                .intendedTurn(Turn.valueOf(TURN))
                .addedSuccessfully(true)
                .build();
    }
}
