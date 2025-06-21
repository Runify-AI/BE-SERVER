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
import java.time.format.DateTimeParseException;
import java.util.List;
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

        Routine routine = Routine.builder()
                .userId(userId)
                .place(routineRequestDTO.getPlace())
                .destination(routineRequestDTO.getDestination())
                .time(time)
                .day(routineRequestDTO.getDay())
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
                .map(RoutineResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRoutine(String token, Long routineId, RoutineRequestDTO routineRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        Routine routine = routineRepository.findByRoutineIdAndUserId(routineId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴을 찾을 수 없습니다."));

        try {
            LocalTime time = LocalTime.parse(routineRequestDTO.getTime());

            routine.update(
                    routineRequestDTO.getPlace(),
                    time,
                    routineRequestDTO.getDay(),
                    routineRequestDTO.getDestination()
            );
            routineRepository.save(routine);
        } catch (DateTimeParseException e) {
            throw new CustomException(ErrorCode.INVALID_ROUTINE_PARAMETER, "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.");
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

