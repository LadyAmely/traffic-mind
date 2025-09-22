package org.shop.traffic.dto.response;

import org.shop.traffic.model.Direction;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record LaneQueueResponse(
        UUID intersectionId,
        Map<Direction, Integer> waitingVehicleCounts,
        LocalDateTime timestamp
) {
}
