package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name="signal_cycle")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction")
    private Direction direction;

    @Column(name = "duration_green")
    private Integer durationGreen;

    @Column(name = "duration_red")
    private Integer durationRed;

    @Column(name = "priority")
    private Integer priority;
}
