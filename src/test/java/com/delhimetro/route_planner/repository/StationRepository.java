package com.delhimetro.route_planner.repository;

import com.delhimetro.route_planner.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByName(String name);

    List<Station> findByIsInterchangeTrue();

    List<Station> findByLineId(Long lineId);

    @Query("SELECT s FROM Station s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Station> searchByName(String keyword);

    // This query loads station AND its line in ONE database call
    // Prevents LazyInitializationException
    @Query("SELECT s FROM Station s JOIN FETCH s.line")
    List<Station> findAllWithLine();
}