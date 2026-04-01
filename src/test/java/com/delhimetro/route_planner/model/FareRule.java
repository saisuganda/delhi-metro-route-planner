package com.delhimetro.route_planner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "fare_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FareRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_distance_km", nullable = false)
    private Double minDistanceKm;

    @Column(name = "max_distance_km", nullable = false)
    private Double maxDistanceKm;

    @Column(name = "fare_amount", nullable = false)
    private Integer fareAmount;

    @Column(name = "network_type")
    private String networkType = "METRO";
}
