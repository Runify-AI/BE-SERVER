package com.example.runity.service;

import com.example.runity.util.JwtUtil;
import com.example.runity.domain.Routine;
import com.example.runity.DTO.RoutineRequestDTO;
import com.example.runity.DTO.RoutineResponseDTO;
import com.example.runity.error.CustomException;
import com.example.runity.constants.ErrorCode;
import com.example.runity.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void createRoutine(String token, RoutineRequestDTO routineRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        LocalTime time = LocalTime.parse(routineRequestDTO.getTime());

        Set<Routine.Coordinate> coordinateSet = new HashSet<>();
        if (routineRequestDTO.getCoordinates() != null) {
            for (RoutineRequestDTO.CoordinateDTO coordinateDTO : routineRequestDTO.getCoordinates()) {
                coordinateSet.add(new Routine.Coordinate(coordinateDTO.getLatitude(), coordinateDTO.getLongitude()));
            }
        }

        Routine routine = Routine.builder()
                .userId(userId)
                .place(routineRequestDTO.getPlace())
                .time(time)
                .day(routineRequestDTO.getDay())
                .coordinates(coordinateSet)
                .build();
        routineRepository.save(routine);
    }

    public List<RoutineResponseDTO> getRoutines(String token) {
        Long userId = jwtUtil.getUserId(token);
        List<Routine> routines = routineRepository.findByUserId(userId);

        if (routines.isEmpty()) {
            throw new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴이 존재하지 않습니다.");
        }

        return routines.stream()
                .map(RoutineResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRoutine(String token, Long routineId, RoutineRequestDTO routineRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        Routine routine = routineRepository.findByRoutineIdAndUserId(routineId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴을 찾을 수 없습니다."));

        try {
            LocalTime time = LocalTime.parse(routineRequestDTO.getTime());

            Set<Routine.Coordinate> coordinateSet = new HashSet<>();
            if (routineRequestDTO.getCoordinates() != null) {
                for (RoutineRequestDTO.CoordinateDTO coordinateDTO : routineRequestDTO.getCoordinates()) {
                    coordinateSet.add(new Routine.Coordinate(coordinateDTO.getLatitude(), coordinateDTO.getLongitude()));
                }
            }

            routine.update(
                    routineRequestDTO.getPlace(),
                    time,
                    routineRequestDTO.getDay(),
                    new ArrayList<>(coordinateSet)
            );
            routineRepository.save(routine);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ROUTINE_PARAMETER, "루틴 요청 값이 유효하지 않습니다.");
        }
    }

    @Transactional
    public void deleteRoutine(String token, Long routineId) {
        Long userId = jwtUtil.getUserId(token);
        Routine routine = routineRepository.findByRoutineIdAndUserId(routineId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴을 찾을 수 없습니다."));

        routineRepository.delete(routine);
    }
}

