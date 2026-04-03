package com.delhimetro.route_planner.graph;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphNode {

    // The station's database ID
    private Long stationId;

    // The station name — "Rajiv Chowk", "Kashmere Gate" etc
    private String stationName;

    // Which metro line this station is on
    private String lineName;

    // GPS coordinates — used by A* algorithm later
    private Double latitude;
    private Double longitude;

    // Is this an interchange station?
    private Boolean isInterchange;
}