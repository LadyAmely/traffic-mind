package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="traffic_density")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDensity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="lane_id")
    private Lane lane;

    @Column(name = "vehicle_count")
    private Integer vehicleCount;

    @Column(name = "average_wait")
    private Float averageWait;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "average_speed")
    private Double averageSpeed;
}
