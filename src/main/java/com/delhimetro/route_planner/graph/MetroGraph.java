package com.delhimetro.route_planner.graph;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class MetroGraph {

    // THE ADJACENCY LIST
    // Key   = station name (String)
    // Value = list of edges going OUT from that station
    // Example: "Rajiv Chowk" → [edge to New Delhi, edge to Patel Chowk]
    private final Map<String, List<GraphEdge>> adjacencyList = new HashMap<>();

    // Also store all nodes by name for quick lookup
    private final Map<String, GraphNode> nodeMap = new HashMap<>();

    // ── Add a station (node) to the graph ────────────────────────────────────
    public void addNode(GraphNode node) {
        // Only add if not already present
        nodeMap.putIfAbsent(node.getStationName(), node);
        adjacencyList.putIfAbsent(node.getStationName(), new ArrayList<>());
    }

    // ── Add a connection (edge) between two stations ──────────────────────────
    public void addEdge(String fromStation, GraphEdge edge) {
        // Make sure the station exists first
        adjacencyList.putIfAbsent(fromStation, new ArrayList<>());
        adjacencyList.get(fromStation).add(edge);
    }

    // ── Get all edges from a station ──────────────────────────────────────────
    public List<GraphEdge> getNeighbours(String stationName) {
        return adjacencyList.getOrDefault(stationName, new ArrayList<>());
    }

    // ── Get a specific node by name ───────────────────────────────────────────
    public GraphNode getNode(String stationName) {
        return nodeMap.get(stationName);
    }

    // ── Get all station names ─────────────────────────────────────────────────
    public Set<String> getAllStationNames() {
        return adjacencyList.keySet();
    }

    // ── Check if station exists ───────────────────────────────────────────────
    public boolean hasStation(String stationName) {
        return adjacencyList.containsKey(stationName);
    }

    // ── Get total number of stations ──────────────────────────────────────────
    public int getTotalStations() {
        return adjacencyList.size();
    }

    // ── Get total number of connections ──────────────────────────────────────
    public int getTotalConnections() {
        return adjacencyList.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    // ── Clear and reset the graph ─────────────────────────────────────────────
    public void clear() {
        adjacencyList.clear();
        nodeMap.clear();
        log.info("Graph cleared");
    }
}