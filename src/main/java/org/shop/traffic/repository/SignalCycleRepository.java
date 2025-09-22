package org.shop.traffic.repository;

import org.shop.traffic.model.SignalCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignalCycleRepository extends JpaRepository<SignalCycle, UUID> {
}
