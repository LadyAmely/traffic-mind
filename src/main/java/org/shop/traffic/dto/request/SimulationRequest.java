package org.shop.traffic.dto.request;
import lombok.Builder;

import java.util.List;

@Builder
public record SimulationRequest(List<Command> commands) {
}
