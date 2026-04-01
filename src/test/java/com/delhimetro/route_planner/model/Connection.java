package com.delhimetro.route_planner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_station_id", nullable = false)
    private Station fromStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_station_id", nullable = false)
    private Station toStation;

    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    @Column(name = "travel_time_mins", nullable = false)
    private Integer travelTimeMins;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
