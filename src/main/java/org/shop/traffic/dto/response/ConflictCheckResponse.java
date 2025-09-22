package org.shop.traffic.dto.response;

import org.shop.traffic.dto.request.ConflictRequest;

import java.util.List;

public record ConflictCheckResponse(
        boolean isSafe,
        List<ConflictRequest> conflicts
) {
}
