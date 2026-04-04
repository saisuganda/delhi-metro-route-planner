package com.delhimetro.route_planner.controller;

import com.delhimetro.route_planner.dto.RouteResponse;
import com.delhimetro.route_planner.graph.MetroGraph;
import com.delhimetro.route_planner.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;
    private final MetroGraph metroGraph;

    // ── ENDPOINT 1: Find shortest route ──────────────────────────────────────
    // URL: GET /api/route?from=Rajiv Chowk&to=HUDA City Centre
    @GetMapping("/route")
    public ResponseEntity<RouteResponse> getRoute(
            @RequestParam String from,
            @RequestParam String to) {

        log.info("API call: /api/route from={} to={}", from, to);
        RouteResponse response = routeService.findRoute(from, to);

        if (response.getPathFound()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ── ENDPOINT 2: List all stations ─────────────────────────────────────────
    // URL: GET /api/stations
    @GetMapping("/stations")
    public ResponseEntity<Map<String, Object>> getAllStations() {
        Set<String> stations = metroGraph.getAllStationNames();

        Map<String, Object> response = new HashMap<>();
        response.put("stations", stations);
        response.put("totalCount", stations.size());

        return ResponseEntity.ok(response);
    }

    // ── ENDPOINT 3: Health check ──────────────────────────────────────────────
    // URL: GET /api/health
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("totalStations", metroGraph.getTotalStations());
        response.put("totalEdges", metroGraph.getTotalConnections());
        response.put("message", "Delhi Metro Route Planner is running");

        return ResponseEntity.ok(response);
    }
}