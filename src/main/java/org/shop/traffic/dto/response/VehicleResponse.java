package org.shop.traffic.dto.response;

import lombok.Builder;
import org.shop.traffic.model.Turn;
import org.shop.traffic.model.VehicleType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record VehicleResponse(
        UUID vehicleId,
        UUID laneId,
        VehicleType vehicleType,
        Turn intendedTurn,
        LocalDateTime arrivalTime,
        boolean addedSuccessfully
) {
}
