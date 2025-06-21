package com.example.runity.repository;

import com.example.runity.domain.PathCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathCoordinateRepository extends JpaRepository<PathCoordinate, Long> {
}
