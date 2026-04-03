package com.delhimetro.route_planner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_interchange")
    private Boolean isInterchange = false;

    private Integer zone = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}