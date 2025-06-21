package com.example.runity.controller;

import com.example.runity.DTO.JwtResponseDTO;
import com.example.runity.DTO.LoginRequestDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.domain.User;
import com.example.runity.service.LoginService;
import com.example.runity.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "이메일 로그인", description = "< 로그인 > API")
public class LoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    //** 서비스 자체 이메일 로그인
    @Operation(summary = "이메일 로그인 시 필요한 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = loginService.login(loginRequestDTO);
        String token = jwtUtil.createAccessToken(loginService.toCustomUserInfoDTO(user));

        return ResponseEntity.ok(JwtResponseDTO.builder()
                .token(token)
                .build());
    }
}
