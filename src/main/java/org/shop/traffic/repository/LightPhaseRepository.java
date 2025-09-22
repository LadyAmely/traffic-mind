package org.shop.traffic.repository;

import org.shop.traffic.model.LightPhase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LightPhaseRepository extends JpaRepository<LightPhase, UUID> {
}
