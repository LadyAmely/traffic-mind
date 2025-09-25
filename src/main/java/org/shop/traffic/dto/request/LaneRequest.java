package org.shop.traffic.dto.request;

import org.shop.traffic.model.Direction;

import java.util.Map;

public record LaneRequest(
        Map<Direction, Map<Integer, Integer>> laneData
) {
}
