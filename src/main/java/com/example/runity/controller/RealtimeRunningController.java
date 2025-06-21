package com.example.runity.controller;

import com.example.runity.DTO.runningTS.RunningCompleteRequest;
import com.example.runity.DTO.runningTS.RunningPathDTO;
import com.example.runity.service.RealtimeRunningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "실시간 러닝 관련")
@CrossOrigin(origins = "*") // 또는 정확한 origin만
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running")
public class RealtimeRunningController {

    private final RealtimeRunningService realtimeRunningService;

    // 5분 주기로 사용자 러닝 정보를 저장
    @PostMapping("/state")
    public ResponseEntity<Void> saveRunningState(@RequestHeader("userId") Long userId,
                                                 @RequestBody RunningPathDTO dto) {
        realtimeRunningService.saveRunningState(userId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "러닝 상태 저장",
            description = "3분 주기로 사용자의 위치 및 상태 데이터를 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "러닝 상태 저장 완료"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 형식 오류")
    })
    @PostMapping("/states")
    public ResponseEntity<Void> saveRunningStates(@Parameter(description = "사용자 ID", required = true)
                                                      @RequestHeader("userId") Long userId,

                                                  @Parameter(description = "경로 ID", required = true)
                                                      @RequestHeader("routeId") Long routeId,

                                                  @Parameter(description = "사용자의 러닝 경로 좌표 리스트", required = true)
                                                      @RequestBody List<RunningPathDTO> dto) {
        realtimeRunningService.saveRunningStates(userId, routeId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "러닝 세션 완료",
            description = "러닝을 종료하고 러닝 경로, 거리, 시간 등의 데이터를 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "러닝 기록 저장 완료"),
            @ApiResponse(responseCode = "400", description = "입력값 오류"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 경로 정보 없음"),
    })
    @PostMapping("/complete")
    public ResponseEntity<Void> completeRunning(@Parameter(description = "사용자 ID", required = true)
                                                    @RequestHeader("userId") Long userId,

                                                @Parameter(description = "러닝 완료 요청 데이터", required = true)
                                                    @RequestBody RunningCompleteRequest request) {
        //realtimeRunningService.completeRunning(token, request);
        realtimeRunningService.completeRunning(userId, request);
        realtimeRunningService.updateDailyRunningRecord(userId, LocalDate.now());
        return ResponseEntity.ok().build();
    }
}
