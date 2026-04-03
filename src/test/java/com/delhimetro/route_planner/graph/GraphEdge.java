package com.delhimetro.route_planner.graph;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphEdge {

    // The station this edge points TO
    private GraphNode destination;

    // Travel time in minutes — this is the WEIGHT
    // Dijkstra uses this to find shortest path
    private Integer travelTimeMins;

    // Distance in km — used for fare calculation
    private Double distanceKm;

    // Which line this connection belongs to
    private String lineName;
}