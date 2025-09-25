package org.shop.traffic.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateIntersectionRequest(
    String name,
    String location,
    BigDecimal latitude,
    BigDecimal longitude
) {
}
