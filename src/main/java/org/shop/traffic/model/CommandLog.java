package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="command_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "command_type")
    private String commandType;

    @Column(name = "vehicle_id")
    private String vehicleId;

    @Column(name = "start_road")
    private Direction startRoad;

    @Column(name = "end_road")
    private Direction endRoad;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;
}

