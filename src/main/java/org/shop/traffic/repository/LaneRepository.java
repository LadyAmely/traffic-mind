package org.shop.traffic.repository;

import org.shop.traffic.model.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LaneRepository extends JpaRepository<Lane, UUID> {
}
