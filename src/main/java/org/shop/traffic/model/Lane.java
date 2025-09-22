package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name="lane")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lane {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @ManyToOne
    private Intersection intersection;
}
