package com.example.runity.tracking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RealTimeTrackingRepository extends JpaRepository<RealTimeTrackingEntity, Long> {
}