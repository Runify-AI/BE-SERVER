package com.example.runity.repository;

import com.example.runity.domain.RealTimeRunning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealTimeRunningRepository extends JpaRepository<RealTimeRunning, Long> {
}
