package com.example.runity.controller;

import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
@Tag(name = "소셜 로그인", description = "< OAuth2 카카오 로그인 테스트 > API")
public class OAuth2LoginController {
    @Operation(summary = "카카오 로그인 성공 테스트 API 입니다. [담당자] : 정현아", description = "카카오 로그인 성공 시 세션에서 JWT 토큰을 반환합니다. [테스트 용도]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 및 토큰 반환"),
            @ApiResponse(responseCode = "401", description = "인증 실패 또는 토큰 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/login-success")
    public String loginSuccess(HttpSession session) {
        String token = (String) session.getAttribute("jwt_token");

        if (token == null) {
            return "로그인 세션 없음 또는 JWT 토큰 누락";
        }
        return "카카오 로그인 성공 <br>JWT Token: " + token;
    }
}
