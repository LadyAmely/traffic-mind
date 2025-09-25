package org.shop.traffic.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shop.traffic.controller.IntersectionController;
import org.shop.traffic.dto.request.VehicleRequest;
import org.shop.traffic.dto.response.VehicleResponse;
import org.shop.traffic.model.Turn;
import org.shop.traffic.model.VehicleType;
import org.shop.traffic.service.IntersectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IntersectionController.class)
class IntersectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IntersectionService trafficSimulationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID TEST_LANE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final VehicleType TEST_VEHICLE_TYPE = VehicleType.CAR;
    private static final float TEST_SPEED = 45.5f;
    private static final float TEST_LENGTH = 4.2f;
    private static final LocalDateTime TEST_ARRIVAL_TIME = LocalDateTime.of(2025, 9, 25, 14, 30);
    private static final Turn TEST_INTENDED_TURN = Turn.LEFT;
    private static final int TEST_PRIORITY = 1;

    private static final UUID TEST_VEHICLE_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final boolean TEST_ADDED_SUCCESSFULLY = true;

    @Test
    @DisplayName("Should return 201 Created when adding a vehicle to a specific intersection")
    void shouldReturn201WhenAddVehicleToIntersection() throws Exception {
        VehicleRequest request = getValidVehicleRequest();
        VehicleResponse response = getValidVehicleResponse();

        given(trafficSimulationService.addVehicle(any(), any())).willReturn(response);

        mockMvc.perform(post("/api/v1/intersection/{id}/vehicle", TEST_LANE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Controller should respond with 201 when vehicle is added to intersection")
    void shouldReturn201WhenCreateVehicle() throws Exception{
        VehicleRequest request = getValidVehicleRequest();
        VehicleResponse response = getValidVehicleResponse();

        given(trafficSimulationService.addVehicle(any(), any())).willReturn(response);

        mockMvc.perform(post("/api/v1/intersection/{id}/vehicle", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("Should return 404 Not Found when intersection ID does not exist")
    void shouldReturn404WhenIntersectionDoesNotExist() throws Exception {
        VehicleRequest request = getValidVehicleRequest();
        UUID nonExistentId = UUID.randomUUID();

        given(trafficSimulationService.addVehicle(any(), eq(nonExistentId)))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Intersection not found"));

        mockMvc.perform(post("/api/v1/intersection/{id}/vehicle", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    private static VehicleRequest getValidVehicleRequest() {
        return VehicleRequest.builder()
                .laneId(TEST_LANE_ID)
                .vehicleType(TEST_VEHICLE_TYPE)
                .speed(TEST_SPEED)
                .length(TEST_LENGTH)
                .arrivalTime(TEST_ARRIVAL_TIME)
                .intendedTurn(TEST_INTENDED_TURN)
                .priority(TEST_PRIORITY)
                .build();
    }

    private static VehicleResponse getValidVehicleResponse() {
        return VehicleResponse.builder()
                .vehicleId(TEST_VEHICLE_ID)
                .laneId(TEST_LANE_ID)
                .vehicleType(TEST_VEHICLE_TYPE)
                .intendedTurn(TEST_INTENDED_TURN)
                .arrivalTime(TEST_ARRIVAL_TIME)
                .addedSuccessfully(TEST_ADDED_SUCCESSFULLY)
                .build();
    }
}