package com.delhimetro.route_planner.graph;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResult {

    // The ordered list of stations from source to destination
    private List<String> path;

    // Total travel time in minutes
    private Integer totalTimeMins;

    // Total distance in km
    private Double totalDistanceKm;

    // Number of stations in the path
    private Integer totalStations;

    // Number of line changes needed
    private Integer interchangeCount;

    // Whether a valid path was found
    private Boolean pathFound;

    // Error message if path not found
    private String errorMessage;
}