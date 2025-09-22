package org.shop.traffic.repository;

import org.shop.traffic.model.TrafficDensity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrafficDensityRepository extends JpaRepository<TrafficDensity, UUID> {
}
