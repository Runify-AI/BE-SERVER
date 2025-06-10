package com.example.runity.repository;

import com.example.runity.domain.RouteChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteChoiceRepository extends JpaRepository<RouteChoice, Long> {
    List<RouteChoice> findAllByRouteRouteId(Long routeId);
}
