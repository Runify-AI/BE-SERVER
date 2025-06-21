package com.example.runity.controller;

import com.example.runity.DTO.PreferenceRequestDTO;
import com.example.runity.DTO.PreferenceResponseDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.service.PreferenceService;
import com.example.runity.constants.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "사용자 선호도", description = "< 선호도 설정 > API")
public class PreferenceController {
    private final PreferenceService preferenceService;

    @Operation(summary = "유저의 선호도를 등록하는 API 입니다. [담당자] : 정현아",
            description = "선호 장소: NONE, PARK, CAFE, RIVER / 선호 경로: NONE, FASTEST, SCENIC, EXERCISE, QUIET / 피하고 싶은 경로: NONE, SLOPE, STAIRS, DARK, ISOLATED / 기타: NONE, PET, ACCESSIBLE, CONVENIENCE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "선호도 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/preferences-create")
    public ResponseEntity<ReturnCodeDTO> createPreference(@RequestHeader("Authorization")String token, @RequestBody PreferenceRequestDTO preferenceRequestDTO){
        preferenceService.createPreference(token, preferenceRequestDTO);
        return ResponseEntity.status(SuccessCode.SUCCESS_PREFERENCE_CREATE.getStatus())
                .body(new ReturnCodeDTO(
                        SuccessCode.SUCCESS_PREFERENCE_CREATE.getStatus(),
                        SuccessCode.SUCCESS_PREFERENCE_CREATE.getMessage()
                ));
    }

    @Operation(summary = "유저의 선호도를 조회하는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선호도 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PreferenceResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "선호도 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/preferences-list")
    public ResponseEntity<PreferenceResponseDTO> getPreference(
            @RequestHeader("Authorization") String token) {
        PreferenceResponseDTO preferenceResponseDTO = preferenceService.getPreference(token);
        return ResponseEntity.ok(preferenceResponseDTO);
    }

    @Operation(summary = "유저의 선호도를 수정하는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선호도 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "선호도 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/preferences-update")
    public ResponseEntity<ReturnCodeDTO> updatePreference(
            @RequestHeader("Authorization") String token,
            @RequestBody PreferenceRequestDTO preferenceRequestDTO) {
        preferenceService.updatePreference(token, preferenceRequestDTO);
        return ResponseEntity.ok(new ReturnCodeDTO(
                SuccessCode.SUCCESS_PREFERENCE_UPDATE.getStatus(),
                SuccessCode.SUCCESS_PREFERENCE_UPDATE.getMessage()
        ));
    }

    @Operation(summary = "유저의 선호도를 삭제하는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선호도 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "선호도 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @DeleteMapping("/preferences-delete")
    public ResponseEntity<ReturnCodeDTO> deletePreference(
            @RequestHeader("Authorization") String token) {
        preferenceService.deletePreference(token);
        return ResponseEntity.ok(
                new ReturnCodeDTO(
                        SuccessCode.SUCCESS_PREFERENCE_DELETE.getStatus(),
                        SuccessCode.SUCCESS_PREFERENCE_DELETE.getMessage()
                )
        );
    }
}
