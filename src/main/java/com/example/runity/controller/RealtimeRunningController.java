package com.example.runity.controller;

import com.example.runity.DTO.runningTS.RunningCompleteRequest;
import com.example.runity.DTO.runningTS.Statics;
import com.example.runity.DTO.runningTS.RunningPathDTO;
import com.example.runity.service.RealtimeRunningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "실시간 러닝 관련", description = "< 러닝 중 위치 저장 / 완료 기록 처리 >")
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running")
public class RealtimeRunningController {

    private final RealtimeRunningService realtimeRunningService;

    @Operation(summary = "단일 러닝 상태 저장", description = "데모 버전에서 사용하지 않는 API입니다. 러닝 상태 리스트 저장을 이용해주세요!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "러닝 상태 저장 완료"),
            @ApiResponse(responseCode = "400", description = "입력값 형식 오류")
    })
    @PostMapping("/state")
    public ResponseEntity<Void> saveRunningState(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @RequestHeader("userId") Long userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "단일 러닝 상태 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RunningPathDTO.class))
            )
            @RequestBody RunningPathDTO dto) {
        realtimeRunningService.saveRunningState(userId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "러닝 상태 리스트 저장", description = "3분 주기로 사용자 상태 좌표 리스트를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "러닝 상태 저장 완료"),
            @ApiResponse(responseCode = "400", description = "입력값 형식 오류")
    })
    @PostMapping("/states")
    public ResponseEntity<Void> saveRunningStates(
            @Parameter(description = "인증 토큰", required = true, example = "Bearer {token}")
            @RequestHeader("Authorization") String token,

            @Parameter(description = "경로 ID", required = true, example = "10")
            @RequestHeader("routeId") Long routeId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "러닝 상태 데이터 리스트",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RunningPathDTO.class))
            )
            @RequestBody RunningPathDTO dto) {
        realtimeRunningService.saveRunningStates(token, routeId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "러닝 완료 및 분석", description = "러닝 종료 시 기록 저장 및 분석 결과를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "러닝 기록 저장 및 피드백 분석 완료"),
            @ApiResponse(responseCode = "400", description = "입력값 오류"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 경로 정보 없음"),
    })
    @PostMapping("/complete")
    public ResponseEntity<Statics> completeRunning(
            @Parameter(description = "인증 토큰", required = true, example = "Bearer {token}")
            @RequestHeader("Authorization") String token,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "러닝 완료 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RunningCompleteRequest.class))
            )
            @RequestBody RunningCompleteRequest request) {

        Long sessionId = realtimeRunningService.completeRunning(token, request);
        Statics feedbackDTO = realtimeRunningService.analyzeRunningStatistics(token, sessionId);
        realtimeRunningService.updateDailyRunningRecord(token, LocalDate.now());

        return ResponseEntity.ok(feedbackDTO);
    }
}