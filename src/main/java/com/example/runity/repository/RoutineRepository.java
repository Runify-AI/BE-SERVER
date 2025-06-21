package com.example.runity.repository;

import com.example.runity.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository< Routine, Long> {
    List<Routine> findByUserId(Long userId);
    Optional<Routine> findByRoutineIdAndUserId(Long routineId, Long userId);
    Routine findByRoutineId(Long routineId);
}