package org.shop.traffic.dto.response;

import org.shop.traffic.dto.request.StepStatus;

import java.util.List;

public record SimulationResponse(
        List<StepStatus> stepStatuses
) {
}
