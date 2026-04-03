package com.delhimetro.route_planner.repository;

import com.delhimetro.route_planner.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByFromStationId(Long stationId);
    List<Connection> findByLineId(Long lineId);

    @Query("SELECT c FROM Connection c JOIN FETCH c.fromStation JOIN FETCH c.toStation JOIN FETCH c.line")
    List<Connection> findAllWithStations();
}