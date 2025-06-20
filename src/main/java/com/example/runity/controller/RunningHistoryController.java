package com.example.runity.controller;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.DTO.RunningHistoryDetailDTO;
import com.example.runity.DTO.RunningSessionDTO;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.service.RunningHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "러닝 기록 가져오기")
@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class RunningHistoryController {

    private final RunningHistoryService runningHistoryService;

    @Operation(
            summary = "월별 누적 러닝 거리 조회",
            description = "특정 사용자에 대한 월별 러닝 거리 데이터를 리스트 형태로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월별 거리 조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력 포맷 오류")
    })
    @GetMapping("/monthly-distances")
    public List<Float> getMonthlyDistances(
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam Long userId,

            @Parameter(description = "조회할 년월 (예: 2025-06)", required = true)
            @RequestParam String yearMonth // e.g. "2025-06"
    ) {
        YearMonth ym = YearMonth.parse(yearMonth); // ISO-8601 기본 포맷
        return runningHistoryService.getMonthlyDistances(userId, ym);
    }

    @Operation(
            summary = "일일 러닝 기록 조회",
            description = "특정 날짜에 해당하는 사용자의 러닝 기록, 원래 경로 좌표, 세션별 기록 및 좌표 리스트까지 포함하여 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "러닝 기록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 날짜의 기록 없음"),
    })
    @GetMapping("/daily")
    public ResponseEntity<List<RunningSessionDTO>> getDailyRecord(@Parameter(description = "사용자 ID", required = true)
                                                                      @RequestParam Long userId,

                                                                  @Parameter(description = "조회할 날짜 (예: 2025-06-20)", required = true)
                                                                      @RequestParam LocalDate date) {
        return ResponseEntity.ok(runningHistoryService.getDailyRecord(userId, date));
    }

    /*
    @GetMapping("/period")
    public ResponseEntity<List<RunningHistoryDetailDTO>> getPeriodRecord(@RequestHeader("Authorization") String token,
                                                                         @RequestParam LocalDate start,
                                                                   @RequestParam LocalDate end) {
        List<RunningHistoryDetailDTO> result = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            try {
                RunningHistoryDetailDTO dto = runningHistoryService.getDailyRecord(token, date);
                result.add(dto);
            } catch (RuntimeException e) {
                // 기록 없는 날짜는 무시
            }
        }
        return ResponseEntity.ok(result);
    }
    */
}
