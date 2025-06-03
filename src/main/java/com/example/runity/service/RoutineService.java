package com.example.runity.service;

import com.example.runity.domain.Routine;
import com.example.runity.DTO.RoutineRequestDTO;
import com.example.runity.DTO.RoutineResponseDTO;
import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import com.example.runity.error.CustomException;
import com.example.runity.constants.ErrorCode;
import com.example.runity.repository.RoutineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {
    private final RoutineRepository routineRepository;

    @Transactional
    public void createRoutine(RoutineRequestDTO routineRequestDTO) {
        LocalTime time = LocalTime.parse(routineRequestDTO.getTime());
        Routine routine = new Routine(routineRequestDTO.getUserId(), routineRequestDTO.getPlace(), time, routineRequestDTO.getDay());
        routineRepository.save(routine);
    }

    public List<RoutineResponseDTO> getRoutines(Long userId) {
        List<Routine> routines = routineRepository.findByUserId(userId);

        if (routines.isEmpty()) {
            throw new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴이 존재하지 않습니다.");
        }

        return routines.stream()
                .map(RoutineResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRoutine(Long userId, Long routineId, RoutineRequestDTO routineRequestDTO) {
        Routine routine = routineRepository.findByRoutineIdAndUserId(routineId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴을 찾을 수 없습니다."));

        try {
            LocalTime time = LocalTime.parse(routineRequestDTO.getTime());
            routine.update(routineRequestDTO.getPlace(), time, routineRequestDTO.getDay());
            routineRepository.save(routine);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ROUTINE_PARAMETER, "루틴 요청 값이 유효하지 않습니다.");
        }
    }

    @Transactional
    public void deleteRoutine(Long userId, Long routineId) {
        Routine routine = routineRepository.findByRoutineIdAndUserId(routineId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴을 찾을 수 없습니다."));

        routineRepository.delete(routine);
    }
}

