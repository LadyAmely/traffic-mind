package org.shop.traffic.dto.request;
import java.util.List;

public record SimulationRequest(List<Command> commands) {
}
