package com.example.runity.repository;

import com.example.runity.domain.PathCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PathCoordinateRepository extends JpaRepository<PathCoordinate, Long> {
    List<PathCoordinate> findAllByPathIdOrderBySequenceAsc(Long pathId);
}
