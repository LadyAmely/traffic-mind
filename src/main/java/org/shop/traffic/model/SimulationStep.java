package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="simulation_step")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationStep {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "step_number")
    private Integer stepNumber;

    @ElementCollection
    @Column(name = "left_vehicle_ids")
    private List<String> leftVehicleIds;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
