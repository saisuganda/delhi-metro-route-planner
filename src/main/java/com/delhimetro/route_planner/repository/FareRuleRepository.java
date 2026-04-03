package com.delhimetro.route_planner.repository;

import com.delhimetro.route_planner.model.FareRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FareRuleRepository extends JpaRepository<FareRule, Long> {

    @Query("SELECT f FROM FareRule f WHERE f.minDistanceKm <= :distance AND f.maxDistanceKm >= :distance AND f.networkType = :networkType")
    Optional<FareRule> findFareForDistance(Double distance, String networkType);
}