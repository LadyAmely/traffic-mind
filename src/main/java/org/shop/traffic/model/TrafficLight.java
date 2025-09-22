package org.shop.traffic.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="traffic_light")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficLight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "light")
    private Light light;

    @ManyToOne
    @JoinColumn(name = "lane")
    private Lane lane;

    @Column(name = "last_changed")
    private LocalDateTime lastChanged;
}
