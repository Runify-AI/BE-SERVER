package com.example.runity.repository;

import com.example.runity.domain.DailyRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRunningRecordRepository extends JpaRepository<DailyRunningRecord, Long> {
}
