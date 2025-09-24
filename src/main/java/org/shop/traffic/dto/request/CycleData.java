package org.shop.traffic.dto.request;

import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Light;

import java.util.Map;

public record CycleData(
        Map<Direction, Light> signals,
        Map<Direction, Integer> greenDurations,
        Map<Direction, Integer> redDurations
) {}

