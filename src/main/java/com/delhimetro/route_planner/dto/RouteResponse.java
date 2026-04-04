package com.delhimetro.route_planner.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {

    // The ordered list of station names from source to destination
    private List<String> path;

    // Total travel time in minutes
    private Integer totalTimeMins;

    // Total distance in kilometres
    private Double totalDistanceKm;

    // Number of stations in the path
    private Integer totalStations;

    // Number of line changes (interchanges)
    private Integer interchangeCount;

    // Calculated fare in rupees
    private Integer fare;

    // True if route was found, false if error
    private Boolean pathFound;

    // Error message if pathFound is false
    private String errorMessage;

    // Human readable summary
    private String summary;
}