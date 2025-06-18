package com.example.runity.repository;

import com.example.runity.domain.RealTimeTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealTimeTrackingRepository extends JpaRepository<RealTimeTracking, Long> {
}
