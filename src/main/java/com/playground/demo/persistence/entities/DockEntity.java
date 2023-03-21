package com.playground.demo.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Table(name = "docks", uniqueConstraints = @UniqueConstraint(columnNames = {"station_id", "dock_number"}))
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = "station")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DockEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", referencedColumnName = "id", nullable = false)
    StationEntity station;

    @Column(name = "dock_number", nullable = false)
    int number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AssetStatus status;
}
