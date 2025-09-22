package org.shop.traffic.dto.request;

import org.shop.traffic.model.Direction;

public record CycleRequest(
        boolean forceUpdate,
        Direction priorityDirection
) {
}
