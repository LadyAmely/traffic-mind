package org.shop.traffic.dto.response;

import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Lane;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record IntersectionResponse(
        UUID id,
        String name,
        String location,
        BigDecimal latitude,
        BigDecimal longitude,
        Map<Direction, Lane> lanes
) {
}
