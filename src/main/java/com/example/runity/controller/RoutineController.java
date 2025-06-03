package com.example.runity.controller;

import com.example.runity.DTO.RoutineRequestDTO;
import com.example.runity.DTO.RoutineResponseDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.error.CustomException;
import com.example.runity.constants.SuccessCode;
import com.example.runity.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/routines")
@Tag(name="루틴",description="루틴 관련 API")
public class RoutineController {
    private final RoutineService routineService;

    @Operation(summary = "루틴 생성", description = "루틴을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "요청 오류", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createRoutine(@RequestBody RoutineRequestDTO routineRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        routineService.createRoutine(routineRequestDTO);
        return new ResponseEntity<>(new ReturnCodeDTO(201, SuccessCode.SUCCESS_ROUTINE_CREATE.getMessage()), HttpStatus.CREATED);
    }

    @Operation(summary = "루틴 조회", description = "사용자의 루틴 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoutineResponseDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "루틴 없음", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getRoutines(@PathVariable Long userId) {
        List<RoutineResponseDTO> routines = routineService.getRoutines(userId);
        return ResponseEntity.ok(routines);
    }

    @Operation(summary = "루틴 수정", description = "루틴을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PutMapping("/{userId}/{routineId}")
    public ResponseEntity<?> updateRoutine(
            @PathVariable Long userId,
            @PathVariable Long routineId,
            @RequestBody RoutineRequestDTO routineRequestDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        routineService.updateRoutine(userId, routineId, routineRequestDTO);
        return ResponseEntity.ok(new ReturnCodeDTO(200, "수정 성공"));
    }

    @Operation(summary = "루틴 삭제", description = "루틴을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공", content = @Content),
            @ApiResponse(responseCode = "404", description = "루틴 없음", content = @Content)
    })
    @DeleteMapping("/{userId}/{routineId}")
    public ResponseEntity<?> deleteRoutine(
            @PathVariable Long userId,
            @PathVariable Long routineId) {
        routineService.deleteRoutine(userId, routineId);
        return ResponseEntity.noContent().build();
    }
}
