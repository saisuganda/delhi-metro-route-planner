package com.delhimetro.route_planner.repository;

import com.delhimetro.route_planner.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    // Find all connections FROM a specific station
    List<Connection> findByFromStationId(Long stationId);

    // Find all connections on a specific line
    List<Connection> findByLineId(Long lineId);

    // Get ALL connections with station details in one query
    // This is the query that builds our graph
    @Query("SELECT c FROM Connection c " +
            "JOIN FETCH c.fromStation " +
            "JOIN FETCH c.toStation " +
            "JOIN FETCH c.line")
    List<Connection> findAllWithStations();
}
