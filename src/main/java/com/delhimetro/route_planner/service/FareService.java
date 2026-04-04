package com.delhimetro.route_planner.service;

import com.delhimetro.route_planner.model.FareRule;
import com.delhimetro.route_planner.repository.FareRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FareService {

    private final FareRuleRepository fareRuleRepository;

    public int calculateFare(Double distanceKm, String networkType) {
        try {
            Optional<FareRule> rule = fareRuleRepository
                .findFareForDistance(distanceKm, networkType);

            if (rule.isPresent()) {
                log.debug("Fare for {}km on {}: ₹{}",
                    distanceKm, networkType, rule.get().getFareAmount());
                return rule.get().getFareAmount();
            }

            // Default fare if no rule found
            log.warn("No fare rule found for {}km, using default", distanceKm);
            return 60;

        } catch (Exception e) {
            log.error("Error calculating fare: {}", e.getMessage());
            return 60;
        }
    }
}