package org.shop.traffic.repository;

import org.shop.traffic.model.SimulationStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SimulationStepRepository extends JpaRepository<SimulationStep, UUID> {
}
