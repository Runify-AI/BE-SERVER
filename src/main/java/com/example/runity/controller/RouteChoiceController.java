package com.example.runity.controller;

import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.domain.RouteChoice;
import com.example.runity.DTO.RouteChoiceRequestDTO;
import com.example.runity.DTO.RouteChoiceResponseDTO;
import com.example.runity.constants.SuccessCode;
import com.example.runity.service.RouteChoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/routes/choices")
@RequiredArgsConstructor
@Tag(name = "경로 설정", description = "< 경로 설정 > API")
public class RouteChoiceController {
    private final RouteChoiceService routeChoiceService;

    @Operation(summary = "경로 설정을 생성하는 API 입니다. [담당자] : 정현아", description = "해당 경로 ID에 대해 사용자 경로 설정(대중교통 이용, 완만한 경사, 붐비지 않은 곳)을 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "경로 설정 생성 성공",
                    content = @Content(schema = @Schema(implementation = RouteChoiceResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/{routeId}")
    public ResponseEntity<ReturnCodeDTO> createRouteChoice(
            @PathVariable Long routeId,
            @RequestBody @Valid RouteChoiceRequestDTO routeChoiceRequestDTO) {
        RouteChoice saved = routeChoiceService.saveChoice(routeId, routeChoiceRequestDTO);

        RouteChoiceResponseDTO routeChoiceResponseDTO = RouteChoiceResponseDTO.builder()
                .choiceId(saved.getChoiceId())
                .usePublicTransport(saved.isUsePublicTransport())
                .preferSafePath(saved.isPreferSafePath())
                .avoidCrowdedAreas(saved.isAvoidCrowdedAreas())
                .build();

        return ResponseEntity.status(SuccessCode.SUCCESS_PREFERENCE_CREATE.getStatus())
                .body(new ReturnCodeDTO(
                        SuccessCode.SUCCESS_PREFERENCE_CREATE.getStatus(),
                        SuccessCode.SUCCESS_PREFERENCE_CREATE.getMessage(),
                        routeChoiceResponseDTO
                ));
    }

    @Operation(summary = "경로 설정을 조회하는 API 입니다. [담당자] : 정현아", description = "경로 ID로 등록된 사용자 경로 설정 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경로 설정 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RouteChoiceResponseDTO.class)))),
            @ApiResponse(responseCode = "404",description = "설정 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/{routeId}")
    public ResponseEntity<ReturnCodeDTO> getRouteChoice(@PathVariable Long routeId) {
        List<RouteChoice> routeChoices = routeChoiceService.getChoiceByRouteId(routeId);

        List<RouteChoiceResponseDTO> response = routeChoices.stream()
                .map(pref -> RouteChoiceResponseDTO.builder()
                        .choiceId(pref.getChoiceId())
                        .usePublicTransport(pref.isUsePublicTransport())
                        .preferSafePath(pref.isPreferSafePath())
                        .avoidCrowdedAreas(pref.isAvoidCrowdedAreas())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ReturnCodeDTO(
                SuccessCode.SUCCESS_PREFERENCE_GET.getStatus(),
                SuccessCode.SUCCESS_PREFERENCE_GET.getMessage(),
                response
        ));
    }
}
