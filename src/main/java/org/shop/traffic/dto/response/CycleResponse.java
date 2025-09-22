package org.shop.traffic.dto.response;

import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Light;

import java.util.Map;

public record CycleResponse(
        Map<Direction, Light> updatedSignals,
        Map<Direction, Integer> greenDurations,
        Map<Direction, Integer> redDurations,
        String strategyUsed
) {
}
