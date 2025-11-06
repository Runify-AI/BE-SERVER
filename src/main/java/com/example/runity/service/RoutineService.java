package com.example.runity.service;

import com.example.runity.domain.Route;
import com.example.runity.repository.RouteRepository;
import com.example.runity.util.JwtUtil;
import com.example.runity.domain.Routine;
import com.example.runity.DTO.route.RoutineRequestDTO;
import com.example.runity.DTO.route.RoutineResponseDTO;
import com.example.runity.error.CustomException;
import com.example.runity.constants.ErrorCode;
import com.example.runity.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final JwtUtil jwtUtil;
    private final RouteRepository routeRepository;

    @Transactional
    public void createRoutine(String token, RoutineRequestDTO routineRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        LocalTime time = LocalTime.parse(routineRequestDTO.getTime());

        Routine routine = Routine.builder()
                .userId(userId)
                .place(routineRequestDTO.getPlace())
                .destination(routineRequestDTO.getDestination())
                .time(time)
                .day(new ArrayList<>(routineRequestDTO.getDay()))
                .build();

        routineRepository.save(routine);
    }

    public List<RoutineResponseDTO> getRoutines(String token) {
        Long userId = jwtUtil.getUserId(token);
        // userID에 해당하는 루틴 목록 조회
        List<Routine> routines = routineRepository.findByUserId(userId);

        /* 비어있을 경우 빈 배열을 반환. 아래 코드 주석 처리
        if (routines.isEmpty()) {
            throw new CustomException(ErrorCode.ROUTINE_NOT_FOUND, "루틴이 존재하지 않습니다.");
        }

         */

        return routines.stream()
                .map(routine -> {
                    RoutineResponseDTO dto = RoutineResponseDTO.from(routine);

                    // [2] 오늘 날짜의 루트 ID를 찾는 로직 호출 및 DTO에 설정
                    Long todayRouteId = findTodayRouteIdForRoutine(userId, routine.getRoutineId());
                    dto.setTodayRouteId(todayRouteId);

                    return dto;
                })
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

    /**
     * [2-1] 특정 루틴이 오늘 실행되었는지 확인하고, 실행되었다면 Route ID를 반환하는 메서드.
     * * @param userId 현재 사용자 ID
     * @param routineId 확인할 루틴 ID
     * @return 오늘 실행된 Route의 ID (없으면 null)
     */
    private Long findTodayRouteIdForRoutine(Long userId, Long routineId) {
        // 1. 오늘 하루의 시작 시간과 끝 시간 계산
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN); // 오늘 00:00:00
        LocalDateTime endOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);   // 오늘 23:59:59.999999999

        // 2. RouteRepository를 사용하여 데이터베이스 조회 (Route.createdAt 기준)
        // findFirstBy...를 사용해 조회
        Optional<Route> todayRoute = routeRepository.findFirstByRoutine_RoutineIdAndUser_UserIdAndCreatedAtBetween(
                routineId,
                userId,
                startOfToday,
                endOfToday
        );

        // 3. 결과 처리: Route가 발견되면 ID를 반환하고, 없으면 null을 반환
        return todayRoute.map(Route::getRouteId).orElse(null);
    }
}

