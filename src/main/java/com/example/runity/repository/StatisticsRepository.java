package com.example.runity.repository;

import com.example.runity.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Optional<Statistics> findByUserId(Long userId);

}
