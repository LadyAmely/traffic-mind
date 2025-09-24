package org.shop.traffic.agent;

import org.shop.traffic.model.Direction;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrafficSignalDQNAgent {

    private final Random random = new Random();
    public record Decision(Direction direction) {}

    public Decision decide(Direction priority) {
        double epsilon = 0.1;
        Direction chosen = random.nextDouble() < epsilon
                ? getRandomDirection()
                : priority;

        return new Decision(chosen);
    }
    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }
}
