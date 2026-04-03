package com.delhimetro.route_planner.service;

import com.delhimetro.route_planner.graph.GraphEdge;
import com.delhimetro.route_planner.graph.GraphNode;
import com.delhimetro.route_planner.graph.MetroGraph;
import com.delhimetro.route_planner.graph.RouteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DijkstraService {

    private final MetroGraph metroGraph;

    // INTERCHANGE PENALTY — extra minutes added when switching lines
    // Rajiv Chowk Yellow→Blue takes extra walking time
    private static final int INTERCHANGE_PENALTY = 5;

    public RouteResult findShortestPath(String sourceName, String destinationName) {

        log.info("Finding route: {} → {}", sourceName, destinationName);

        // ── Validation ────────────────────────────────────────────────────────
        if (!metroGraph.hasStation(sourceName)) {
            return errorResult("Station not found: " + sourceName);
        }
        if (!metroGraph.hasStation(destinationName)) {
            return errorResult("Station not found: " + destinationName);
        }
        if (sourceName.equalsIgnoreCase(destinationName)) {
            return errorResult("Source and destination cannot be the same");
        }

        // ── Data structures for Dijkstra ──────────────────────────────────────

        // dist[] — stores shortest known time to reach each station
        // Starts as infinity for all stations except source = 0
        Map<String, Integer> dist = new HashMap<>();

        // parent[] — stores which station we came from
        // Used to reconstruct the path at the end
        Map<String, String> parent = new HashMap<>();

        // lineTaken[] — stores which line we used to reach each station
        // Used to count interchanges
        Map<String, String> lineTaken = new HashMap<>();

        // Priority Queue — always processes the NEAREST unvisited station first
        // This is what makes Dijkstra efficient
        // PriorityQueue sorts by distance (smallest first)
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[1])
        );

        // Initialise all distances to infinity
        for (String station : metroGraph.getAllStationNames()) {
            dist.put(station, Integer.MAX_VALUE);
            parent.put(station, null);
        }

        // Source station distance = 0
        dist.put(sourceName, 0);

        // Add source to priority queue: [stationName hash, distance]
        // We store station name as a string separately
        Map<Integer, String> indexToName = new HashMap<>();
        pq.offer(new int[]{sourceName.hashCode(), 0});
        indexToName.put(sourceName.hashCode(), sourceName);

        // ── Main Dijkstra Loop ────────────────────────────────────────────────
        Set<String> visited = new HashSet<>();

        while (!pq.isEmpty()) {

            // Pick the station with smallest distance
            int[] current = pq.poll();
            String currentName = indexToName.get(current[0]);
            int currentDist = current[1];

            // Skip if already visited
            if (visited.contains(currentName)) continue;
            visited.add(currentName);

            // If we reached the destination — stop early
            if (currentName.equals(destinationName)) break;

            // Explore all neighbours of current station
            for (GraphEdge edge : metroGraph.getNeighbours(currentName)) {

                String neighbourName = edge.getDestination().getStationName();
                if (visited.contains(neighbourName)) continue;

                // Calculate new distance through current station
                int newDist = currentDist + edge.getTravelTimeMins();

                // Add interchange penalty if switching lines
                String currentLine    = lineTaken.getOrDefault(currentName, "");
                String neighbourLine  = edge.getLineName();
                if (!currentLine.isEmpty() && !currentLine.equals(neighbourLine)) {
                    newDist += INTERCHANGE_PENALTY;
                }

                // If this new distance is better — update
                if (newDist < dist.get(neighbourName)) {
                    dist.put(neighbourName, newDist);
                    parent.put(neighbourName, currentName);
                    lineTaken.put(neighbourName, edge.getLineName());

                    int key = neighbourName.hashCode() + newDist;
                    indexToName.put(key, neighbourName);
                    pq.offer(new int[]{key, newDist});
                }
            }
        }

        // ── Path not found ────────────────────────────────────────────────────
        if (dist.get(destinationName) == Integer.MAX_VALUE) {
            return errorResult("No path found between " + sourceName + " and " + destinationName);
        }

        // ── Reconstruct path by tracing parent map backwards ─────────────────
        List<String> path = new ArrayList<>();
        String current = destinationName;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path); // Reverse to get source → destination order

        // ── Count interchanges ────────────────────────────────────────────────
        int interchanges = 0;
        String previousLine = "";
        for (int i = 1; i < path.size(); i++) {
            String line = lineTaken.getOrDefault(path.get(i), "");
            if (!previousLine.isEmpty() && !line.equals(previousLine)) {
                interchanges++;
            }
            previousLine = line;
        }

        // ── Calculate total distance ──────────────────────────────────────────
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to   = path.get(i + 1);
            for (GraphEdge edge : metroGraph.getNeighbours(from)) {
                if (edge.getDestination().getStationName().equals(to)) {
                    totalDistance += edge.getDistanceKm();
                    break;
                }
            }
        }

        log.info("Route found: {} stations, {} mins, {} interchanges",
                path.size(), dist.get(destinationName), interchanges);

        return new RouteResult(
                path,
                dist.get(destinationName),
                Math.round(totalDistance * 10.0) / 10.0,
                path.size(),
                interchanges,
                true,
                null
        );
    }

    // Helper method for error results
    private RouteResult errorResult(String message) {
        log.warn("Route error: {}", message);
        return new RouteResult(null, 0, 0.0, 0, 0, false, message);
    }
}