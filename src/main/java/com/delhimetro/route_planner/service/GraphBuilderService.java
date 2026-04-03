package com.delhimetro.route_planner.service;

import com.delhimetro.route_planner.graph.GraphEdge;
import com.delhimetro.route_planner.graph.GraphNode;
import com.delhimetro.route_planner.graph.MetroGraph;
import com.delhimetro.route_planner.model.Connection;
import com.delhimetro.route_planner.model.Station;
import com.delhimetro.route_planner.repository.ConnectionRepository;
import com.delhimetro.route_planner.repository.StationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GraphBuilderService {

    private final StationRepository stationRepository;
    private final ConnectionRepository connectionRepository;
    private final MetroGraph metroGraph;

    // PostConstruct calls the transactional method separately
    // This is required because @Transactional does not work
    // directly on @PostConstruct methods in Spring
    @PostConstruct
    public void init() {
        buildGraph();
    }

    @Transactional(readOnly = true)
    public void buildGraph() {
        log.info("Building metro graph...");
        metroGraph.clear();

        // Load stations using a JOIN FETCH query
        // so line data is loaded in the same query
        List<Station> allStations = stationRepository.findAllWithLine();
        for (Station station : allStations) {
            String lineName = station.getLine().getName();
            GraphNode node = new GraphNode(
                station.getId(),
                station.getName(),
                lineName,
                station.getLatitude(),
                station.getLongitude(),
                station.getIsInterchange()
            );
            metroGraph.addNode(node);
        }
        log.info("Loaded {} stations into graph", allStations.size());

        List<Connection> allConnections = connectionRepository.findAllWithStations();
        for (Connection connection : allConnections) {
            String fromName = connection.getFromStation().getName();
            String toName   = connection.getToStation().getName();
            String lineName = connection.getLine().getName();

            GraphEdge forwardEdge = new GraphEdge(
                metroGraph.getNode(toName),
                connection.getTravelTimeMins(),
                connection.getDistanceKm(),
                lineName
            );
            metroGraph.addEdge(fromName, forwardEdge);

            GraphEdge backwardEdge = new GraphEdge(
                metroGraph.getNode(fromName),
                connection.getTravelTimeMins(),
                connection.getDistanceKm(),
                lineName
            );
            metroGraph.addEdge(toName, backwardEdge);
        }

        log.info("Graph built successfully!");
        log.info("Total stations : {}", metroGraph.getTotalStations());
        log.info("Total edges    : {}", metroGraph.getTotalConnections());
    }
}