package org.shop.traffic.dto.request;

import java.math.BigDecimal;

public record CreateIntersectionRequest(
    String name,
    String location,
    BigDecimal latitude,
    BigDecimal longitude
) {
}
