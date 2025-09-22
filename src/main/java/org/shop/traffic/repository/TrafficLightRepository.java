package org.shop.traffic.repository;

import org.shop.traffic.model.TrafficLight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrafficLightRepository extends JpaRepository<TrafficLight, UUID> {
}
