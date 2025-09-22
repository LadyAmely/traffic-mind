package org.shop.traffic.repository;

import org.shop.traffic.model.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IntersectionRepository extends JpaRepository<Intersection, UUID> {
}
