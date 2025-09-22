package org.shop.traffic.dto.request;

import org.shop.traffic.model.Direction;
import org.shop.traffic.model.Turn;

public record ConflictRequest(
        Direction fromDirection,
        Turn fromTurn,
        Direction conflictingDirection,
        Turn conflictingTurn,
        String reason
) {
}
