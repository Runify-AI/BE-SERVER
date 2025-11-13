package com.example.runity.repository;

import com.example.runity.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByUserUserIdAndCompletedFalse(Long userId);
    Optional<Route> findByRouteId(Long routeId);
    Optional<Route> findFirstByRoutine_RoutineIdAndUser_UserIdAndCreatedAtBetween(
            Long routineId,
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );
    // 오늘 생성된 미완료 경로를 상세 정보를 포함하여 조회하는 쿼리
    @Query("SELECT r FROM Route r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH r.routine rt " +
            "LEFT JOIN FETCH r.routeChoices rc " +
            "WHERE r.user.userId = :userId " +
            "AND r.completed = FALSE " +
            "AND r.createdAt BETWEEN :startOfToday AND :endOfToday")
    List<Route> findRoutesCreatedTodayWithDetails(
            @Param("userId") Long userId,
            @Param("startOfToday") LocalDateTime startOfToday,
            @Param("endOfToday") LocalDateTime endOfToday);
}
