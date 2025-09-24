package org.shop.traffic.repository;

import org.shop.traffic.model.CommandLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommandLogRepository extends JpaRepository<CommandLog, UUID> {
}
