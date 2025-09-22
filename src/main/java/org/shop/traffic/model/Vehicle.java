package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="vehicle")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "lane_id")
    private Lane lane;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle")
    private VehicleType vehicleType;

    @Column(name = "speed")
    private Float speed;

    @Column(name = "length")
    private Float length;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "has_passed")
    private Boolean hasPassed;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "waiting_time")
    private Float waitingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "intended_turn")
    private Turn turn;
}
