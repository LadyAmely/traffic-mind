package org.shop.traffic.unit.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.traffic.dto.request.Command;
import org.shop.traffic.dto.request.SimulationRequest;
import org.shop.traffic.dto.request.StepStatus;
import org.shop.traffic.dto.response.SimulationResponse;
import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Vehicle;
import org.shop.traffic.repository.CommandLogRepository;
import org.shop.traffic.repository.VehicleRepository;
import org.shop.traffic.service.TrafficSimulationService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrafficSimulationServiceTest {

    @InjectMocks
    private TrafficSimulationService simulationService;

    @Mock
    private CommandLogRepository commandLogRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    private static final String VEHICLE_ID_1 = "Vehicle-A";
    private static final String VEHICLE_ID_2 = "Vehicle-B";
    private static final String COMMAND_ADD = "addVehicle";
    private static final String COMMAND_STEP = "step";
    private static final Direction START_ROAD = Direction.NORTH;
    private static final Direction END_ROAD = Direction.SOUTH;

    @Test
    @DisplayName("should run simulation and return step statuses")
    void shouldRunSimulationAndReturnStepStatuses() {
        // given
        UUID vehicleId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Command addVehicle1 = createCommand(COMMAND_ADD, VEHICLE_ID_1);
        Command addVehicle2 = createCommand(COMMAND_ADD, VEHICLE_ID_2);
        Command step1 = createCommand(COMMAND_STEP, null);

        SimulationRequest request = SimulationRequest.builder()
                .commands(List.of(addVehicle1, addVehicle2, step1))
                .build();

        when(vehicleRepository.save(any())).thenAnswer(invocation -> {
            Vehicle input = invocation.getArgument(0);
            input.setId(vehicleId);
            return input;
        });

        // when
        SimulationResponse response = simulationService.runSimulation(request);

        // then
        assertNotNull(response);
        assertEquals(1, response.stepStatuses().size());

        for (StepStatus status : response.stepStatuses()) {
            assertNotNull(status.leftVehicles());
            assertTrue(status.leftVehicles().size() >= 0);
            assertTrue(status.leftVehicles().contains(vehicleId.toString()));
        }

        verify(commandLogRepository, times(3)).save(any());
    }

    @Test
    @DisplayName("")
    void shouldHandleEmptyCommandList() {
        // given
        SimulationRequest request = SimulationRequest.builder()
                .commands(Collections.emptyList())
                .build();
        // when
        SimulationResponse response = simulationService.runSimulation(request);
        // then
        assertNotNull(response);
        assertTrue(response.stepStatuses().isEmpty());
    }

    private Command createCommand(String type, String vehicleId) {
        return new Command(type, vehicleId, START_ROAD, END_ROAD);
    }
}
