package com.example.runity.repository;

import com.example.runity.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByUserUserIdAndCompletedFalse(Long userId);
    Optional<Route> findByRouteId(Long routeId);
    Optional<Route> findFirstByRoutine_RoutineIdAndUser_UserIdAndCreatedAtBetween(
            Long routineId,
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );
}
