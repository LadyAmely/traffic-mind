package org.shop.traffic.dto.request;

import org.shop.traffic.model.Turn;
import org.shop.traffic.model.VehicleType;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleRequest(
        UUID laneId,
        VehicleType vehicleType,
        Float speed,
        Float length,
        LocalDateTime arrivalTime,
        Turn intendedTurn,
        Integer priority
) {
}
