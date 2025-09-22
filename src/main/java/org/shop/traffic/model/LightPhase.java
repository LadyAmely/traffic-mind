package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name="light_phase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LightPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "traffic_light")
    private TrafficLight trafficLight;

    @Enumerated(EnumType.STRING)
    @Column(name = "light")
    private Light light;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "order")
    private Integer order;
}
