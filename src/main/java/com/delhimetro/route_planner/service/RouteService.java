package com.delhimetro.route_planner.service;

import com.delhimetro.route_planner.dto.RouteResponse;
import com.delhimetro.route_planner.graph.RouteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final DijkstraService dijkstraService;
    private final FareService fareService;

    public RouteResponse findRoute(String source, String destination) {

        log.info("Route request: {} → {}", source, destination);

        // Input validation
        if (source == null || source.trim().isEmpty()) {
            return errorResponse("Source station name cannot be empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            return errorResponse("Destination station name cannot be empty");
        }

        // Run Dijkstra
        RouteResult result = dijkstraService.findShortestPath(
            source.trim(), destination.trim()
        );

        // If no path found return error
        if (!result.getPathFound()) {
            return errorResponse(result.getErrorMessage());
        }

        // Calculate fare
        int fare = fareService.calculateFare(
            result.getTotalDistanceKm(), "METRO"
        );

        // Build summary string
        String summary = String.format(
            "%s → %s  |  %d stations  |  %d mins  |  %.1f km  |  ₹%d",
            source.trim(),
            destination.trim(),
            result.getTotalStations(),
            result.getTotalTimeMins(),
            result.getTotalDistanceKm(),
            fare
        );

        log.info("Route found: {}", summary);

        // Build and return response
        RouteResponse response = new RouteResponse();
        response.setPath(result.getPath());
        response.setTotalTimeMins(result.getTotalTimeMins());
        response.setTotalDistanceKm(result.getTotalDistanceKm());
        response.setTotalStations(result.getTotalStations());
        response.setInterchangeCount(result.getInterchangeCount());
        response.setFare(fare);
        response.setPathFound(true);
        response.setErrorMessage(null);
        response.setSummary(summary);

        return response;
    }

    private RouteResponse errorResponse(String message) {
        log.warn("Route error: {}", message);
        RouteResponse response = new RouteResponse();
        response.setPathFound(false);
        response.setErrorMessage(message);
        response.setPath(null);
        response.setTotalTimeMins(0);
        response.setTotalDistanceKm(0.0);
        response.setTotalStations(0);
        response.setInterchangeCount(0);
        response.setFare(0);
        response.setSummary("Route not found: " + message);
        return response;
    }
}