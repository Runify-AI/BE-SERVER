package com.example.runity.controller;

import com.example.runity.DTO.route.RunningSettingsResponse;
import com.example.runity.service.RunningSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "경로 설정값 가져오기")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running-setting")
public class RunningSettingController {

    private final RunningSettingService runningSettingService;

    @Operation(
            summary = "러닝 세션 설정 정보 조회",
            description = "routeId를 기반으로 러닝 세팅 정보를 조회합니다. 출발지, 도착지, 경로 좌표, 기간, 예상 시간, 러닝 페이스 등의 정보를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 러닝 설정 정보 조회 완료"),
            @ApiResponse(responseCode = "404", description = "해당 routeId로 등록된 경로 없음"),
    })
    @GetMapping
    public ResponseEntity<RunningSettingsResponse> getRunningSetting(@RequestParam Long routeId) {
        RunningSettingsResponse response = runningSettingService.getRunningSettings(routeId);
        return ResponseEntity.ok(response);
    }
}
