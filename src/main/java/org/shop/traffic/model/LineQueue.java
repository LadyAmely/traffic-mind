package org.shop.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="line_queue")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineQueue {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @JdbcTypeCode(SqlTypes.UUID)
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "lane")
        private Lane lane;

        @OneToMany
        @JoinColumn(name = "vehicles")
        private List<Vehicle> vehicles;

}
