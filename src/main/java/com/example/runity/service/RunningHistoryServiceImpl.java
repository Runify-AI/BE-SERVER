package com.example.runity.service;

import com.example.runity.DTO.*;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.domain.RealTimeRunning;
import com.example.runity.domain.RunningPathTS;
import com.example.runity.domain.Route;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.RunningPathTSRepository;
import com.example.runity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningHistoryServiceImpl implements RunningHistoryService {

    private final DailyRunningRecordRepository dailyRunningRecordRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final RunningPathTSRepository runningPathTSRepository;
    private final RunningSettingServiceImpl runningSettingServiceImpl;
    private final RouteRepository routeRepository;
    private final JwtUtil jwtUtil;

    /**
     * 특정 날짜의 러닝 기록 조회
     */
    @Override
    public List<RunningSessionDTO> getDailyRecord(Long userId, LocalDate date) {
        //Long userId = jwtUtil.getUserId(token);

        System.out.println("🧪 All Routes:");
        for (Route r : routeRepository.findAll()) {
            System.out.println("routeId: " + r.getRouteId() + ", userId: " + r.getUser().getUserId());
        }

        // 1. 날짜에 해당하는 DailyRunningRecord 조회
        Optional<DailyRunningRecord> dailyRecord = dailyRunningRecordRepository.findByUserIdAndDate(userId, date);
        DailyRunningRecord record = dailyRecord
                .orElseThrow(() -> new RuntimeException("해당 날짜의 DailyRunningRecord가 존재하지 않습니다."));

        List<RunningSessionDTO> sessions = new ArrayList<>();

        Long recordId = record.getRecordId();

        // 2. 해당 기록에 대한 실시간 러닝 데이터 조회
            List<RealTimeRunning> realTimeList = realTimeRunningRepository.findByRecordId(recordId);

            for (RealTimeRunning realTime : realTimeList) {
                Long sessionId = realTime.getRunningSessionId();
                Long routeId = realTime.getRouteId();

                // 3. 러닝 세팅 정보 가져오기
                if (routeId == null) {
                    throw new RuntimeException("routeId is null for RealTimeRunning.sessionId = " + sessionId);
                }

                RunningSettingsResponse settings = null;
                try {
                    settings = runningSettingServiceImpl.getRunningSettings(routeId);
                } catch (RuntimeException e) {
                    System.out.println("⚠ Route not found for routeId: " + routeId + ", sessionId: " + sessionId);
                    e.printStackTrace();
                    continue;
                }

                // 4. 경로 시간 기반 세부 이력 정보 조회
                List<RunningPathTS> pathList = runningPathTSRepository.findBySessionId(sessionId);
                List<RunningHistoryDetailDTO> detailDTOList = new ArrayList<>();

                for (RunningPathTS path : pathList) {
                    System.out.println("Lat: " + path.getLatitude() + ", Lon: " + path.getLongitude());
                }

                for (RunningPathTS path : pathList) {
                    LocasionDTO location = LocasionDTO.builder()
                            .lat(path.getLatitude())
                            .lon(path.getLongitude())
                            .build();


                    RunningHistoryDetailDTO detail = RunningHistoryDetailDTO.builder()
                            .distance(path.getDistance())
                            .elapsedTime(path.getElapsedTime())
                            .pace((float) path.getPace())
                            .timeStamp(path.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toLocalTime())
                            .location(location)
                            .type(path.getType())
                            .semiType(path.getSemiType())
                            .message(path.getMessage())
                            .build();

                    detailDTOList.add(detail);
                }

                // 5. 러닝 이력 DTO 생성
                RunningHistoryDTO history = RunningHistoryDTO.builder()
                        .averagePace(realTime.getAvgPace())
                        .comment(realTime.getComment())
                        .completedTime(realTime.getEndTime() != null
                                ? realTime.getEndTime().atZone(java.time.ZoneId.systemDefault()).toLocalTime()
                                : null)
                        .effortLevel(realTime.getEffortLevel())
                        .elapsedTime(realTime.getRunTime())
                        .routeId(routeId)
                        .totalDistance(realTime.getDistance())
                        .runningTrackPoint(detailDTOList)
                        .build();

                // 6. 최종 세션 DTO 구성
                RunningSessionDTO sessionDTO = RunningSessionDTO.builder()
                        .runningSettingsResponse(settings)
                        .runningHistoryDTO(history)
                        .build();

                sessions.add(sessionDTO);
            }

        return sessions;
    }



    public RunningHistoryDTO convertToDto(DailyRunningRecord record) {
        return RunningHistoryDTO.builder()
                .elapsedTime(record.getTotalRunTime())
                .totalDistance(record.getTotalDistance())
                .runningTrackPoint(Collections.emptyList())
                .build();
    }



    /**
     * 시작~종료 날짜 구간의 기록 전체 조회 (전체 사용자 기준)
     */
    @Override
    public List<RunningHistoryDTO> getPeriodRecord(String token, LocalDate start, LocalDate end) {
        Long userId = jwtUtil.getUserId(token);
        return dailyRunningRecordRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 기준으로 날짜 구간에 해당하는 기록 조회
     * TODO: AI 사용 통계 반환
     */
    @Override
    public List<RunningHistoryDTO> getUserRunningHistories(String token, LocalDate start, LocalDate end) {
        Long userId = jwtUtil.getUserId(token);
        return dailyRunningRecordRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
