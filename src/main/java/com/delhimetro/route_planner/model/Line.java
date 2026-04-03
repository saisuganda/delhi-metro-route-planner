package com.delhimetro.route_planner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "colour_code", nullable = false)
    private String colourCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NetworkType type;

    @Column(name = "total_stations")
    private Integer totalStations = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NetworkType {
        METRO, RRTS
    }
}