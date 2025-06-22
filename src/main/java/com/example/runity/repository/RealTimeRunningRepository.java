package com.example.runity.repository;

import com.example.runity.domain.RealTimeRunning;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RealTimeRunningRepository extends JpaRepository<RealTimeRunning, Long> {
    @Query("""
    SELECT r FROM RealTimeRunning r
    JOIN DailyRunningRecord d ON r.recordId = d.recordId
    WHERE d.userId = :userId AND d.date = :date
    """)
    List<RealTimeRunning> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    List<RealTimeRunning> findByRecordId(Long recordId);

    Optional<RealTimeRunning> findByRouteIdAndIsCompleted(Long routeId, boolean isCompleted);

    RealTimeRunning findByRunningSessionId(Long runningSessionId);
}
