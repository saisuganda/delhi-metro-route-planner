package com.delhimetro.route_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.delhimetro.route_planner.model")
@EnableJpaRepositories(basePackages = "com.delhimetro.route_planner.repository")
public class RoutePlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoutePlannerApplication.class, args);
	}

}