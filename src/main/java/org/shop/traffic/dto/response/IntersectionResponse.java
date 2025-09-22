package org.shop.traffic.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record IntersectionResponse(
        UUID id,
        String name,
        String location,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
