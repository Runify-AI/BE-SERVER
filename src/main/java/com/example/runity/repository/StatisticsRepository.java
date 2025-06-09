package com.example.runity.repository;

import com.example.runity.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
}
