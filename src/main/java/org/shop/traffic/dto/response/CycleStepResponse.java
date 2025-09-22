package org.shop.traffic.dto.response;

import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Light;

import java.util.List;

public record CycleStepResponse(
        Direction activeDirection,
        Light signal,
        int durationSeconds,
        List<String> affectedLanes,
        List<String> vehiclesAllowedToMove
) {
}
