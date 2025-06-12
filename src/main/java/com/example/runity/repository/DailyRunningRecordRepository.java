package com.example.runity.repository;

import com.example.runity.domain.DailyRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRunningRecordRepository extends JpaRepository<DailyRunningRecord, Long> {
    Optional<DailyRunningRecord> findByUserIdAndDate(Long userId, LocalDate date);

    Optional<DailyRunningRecord> findByDate(LocalDate date);

    List<DailyRunningRecord> findAllByDateBetween(LocalDate start, LocalDate end);

    List<DailyRunningRecord> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

}
