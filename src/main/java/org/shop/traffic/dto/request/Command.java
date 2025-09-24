package org.shop.traffic.dto.request;

import org.shop.traffic.model.Direction;

public record Command(
        String type,
        String vehicleId,
        Direction startRoad,
        Direction endRoad
) {}

