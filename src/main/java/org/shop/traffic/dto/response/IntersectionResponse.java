package org.shop.traffic.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record IntersectionResponse(
        UUID id,
        String name,
        String location,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
