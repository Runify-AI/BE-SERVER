package com.example.runity.repository;

import com.example.runity.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByUserUserId(Long userId);
}
