package com.example.runity.controller;

import com.example.runity.DTO.RoutineRequestDTO;
import com.example.runity.DTO.RoutineResponseDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.constants.SuccessCode;
import com.example.runity.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name="루틴",description=" < 루틴 설정 > API")
public class RoutineController {

    private final RoutineService routineService;

    @Operation(summary = "고정 루틴을 생성하는 API 입니다. [담당자] : 정현아", description = "장소: COMPANY, GYM, SCHOOL, HOME, ETC / 요일: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "루틴 생성 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "요청 오류", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/routines-create")
    public ResponseEntity<ReturnCodeDTO> createRoutine(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid RoutineRequestDTO routineRequestDTO) {

        routineService.createRoutine(token, routineRequestDTO);
        return ResponseEntity.status(SuccessCode.SUCCESS_ROUTINE_CREATE.getStatus())
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTINE_CREATE.getStatus(), SuccessCode.SUCCESS_ROUTINE_CREATE.getMessage()));
    }

    @Operation(summary = "고정 루틴을 조회하는 API 입니다. [담당자] : 정현아", description = "사용자의 루틴 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "루틴 조회 성공", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoutineResponseDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "루틴 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/routines-list")
    public ResponseEntity<ReturnCodeDTO> getRoutines(@RequestHeader("Authorization") String token) {
        List<RoutineResponseDTO> routines = routineService.getRoutines(token);
        return ResponseEntity.ok(new ReturnCodeDTO(
                SuccessCode.SUCCESS_ROUTINE_LIST.getStatus(),
                SuccessCode.SUCCESS_ROUTINE_LIST.getMessage(),
                routines
        ));
    }

    @Operation(summary = "고정 루틴을 수정하는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "루틴 수정 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/routines-update/{routineId}")
    public ResponseEntity<ReturnCodeDTO> updateRoutine(
            @RequestHeader("Authorization") String token,
            @PathVariable Long routineId,
            @RequestBody @Valid RoutineRequestDTO routineRequestDTO) {

        routineService.updateRoutine(token, routineId, routineRequestDTO);
        return ResponseEntity.ok(new ReturnCodeDTO(
                SuccessCode.SUCCESS_ROUTINE_UPDATE.getStatus(),
                SuccessCode.SUCCESS_ROUTINE_UPDATE.getMessage()
        ));
    }

    @Operation(summary = "고정 루틴을 삭제하는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "루틴 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "404", description = "루틴 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @DeleteMapping("/routines-delete/{routineId}")
    public ResponseEntity<ReturnCodeDTO> deleteRoutine(
            @RequestHeader ("Authorization") String token,
            @PathVariable Long routineId) {
        routineService.deleteRoutine(token, routineId);
        return ResponseEntity.status(SuccessCode.SUCCESS_ROUTINE_DELETE.getStatus())
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTINE_DELETE.getStatus(), SuccessCode.SUCCESS_ROUTINE_DELETE.getMessage()));
    }
}
