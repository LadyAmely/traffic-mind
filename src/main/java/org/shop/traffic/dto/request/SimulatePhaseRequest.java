package org.shop.traffic.dto.request;

import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Light;

public record SimulatePhaseRequest(
        Direction direction,
        Light signal,
        int durationSeconds
) {
}
