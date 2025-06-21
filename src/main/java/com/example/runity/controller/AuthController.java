package com.example.runity.controller;

import com.example.runity.DTO.*;
import com.example.runity.constants.ErrorCode;
import com.example.runity.constants.SuccessCode;
import com.example.runity.error.CustomException;
import com.example.runity.repository.VerificationCodeRepository;
import com.example.runity.service.EmailLookupService;
import com.example.runity.service.EmailService;
import com.example.runity.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "회원가입 및 인증", description = "< 회원가입 / 인증 > API")
public class AuthController {
    private final SignupService signupService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailLookupService emailLookupService;

    //* 이메일 인증 코드 발송
    @Operation(summary = "회원가입 시 이메일로 인증코드 보내는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 전송 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerificationCodeRequestDTO verificationCodeRequestDTO) {
        try {
            signupService.createVerificationCodeAndSendEmail(verificationCodeRequestDTO.getEmail());
            return ResponseEntity.ok(SuccessCode.SUCCESS_EMAIL_SENT.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.EMAIL_ALREADY_REGISTERED.getMessage());
        }
    }
    //* 이메일 인증 확인
    @Operation(summary = "이메일 인증코드 확인하는 API 입니다. [담당자] : 정현아", description = "모든 인증코드 확인은 해당 API를 이용해주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 인증 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("email") String email,
                                        @RequestParam("code") String code) {
        try {
            signupService.verifyEmail(email, code);
            return ResponseEntity.ok(SuccessCode.EMAIL_VERIFIED.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.INVALID_VERIFICATION_CODE.getMessage());
        }
    }
    //* 인증 코드 재생성 요청 (이메일로 새로운 코드 발송)
    @Operation(summary = "인증코드 만료 시 재발급하는 API 입니다. [담당자] : 정현아", description = "이메일 인증코드 만료시간은 5분입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 재전송 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody VerificationCodeRequestDTO requestDTO) {
        try {
            // 기존 인증 코드 삭제
            verificationCodeRepository.deleteByEmail(requestDTO.getEmail());
            // 새로운 인증 코드 생성 및 이메일 발송
            signupService.createVerificationCodeAndSendEmail(requestDTO.getEmail());
            return ResponseEntity.ok(SuccessCode.SUCCESS_RESET_CODE_SENT.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }
    //* 회원가입 및 유저 등록
    @Operation(summary = "회원가입 API 입니다. [담당자] : 정현아",
            description = "회원가입 전 인증코드를 먼저 보내주세요! / 비밀번호: 영문+숫자+특수문자 8~15자 / 이름: 1~10자 / 닉네임: 1~10자 / 키: 50cm 이상 / 몸무게: 10kg 이상 / 러닝타입: JOGGING, HALF_MARATHON, RUNNING, TRAIL_RUNNING, INTERVAL_TRAINING")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDTO requestDTO) {
        try {
            signupService.signupUser(requestDTO);
            return ResponseEntity.ok(SuccessCode.SUCCESS_SIGNUP.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.EMAIL_CONFLICT.getMessage());
        }
    }
    // 이메일 찾기
    @Operation(summary = "닉네임과 러닝타입으로 가입된 이메일을 찾는 API 입니다. [담당자]: 정현아",
            description = "회원가입 시 입력한 닉네임과 러닝타입을 기반으로 이메일을 찾아줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 조회 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmailLookupResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "해당 정보로 가입된 이메일 없음", content = {@Content(mediaType = "string")}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/find-email")
    public ResponseEntity<?> findBynickNameAndRunningType(@RequestBody @Valid EmailLoookupRequestDTO emailLoookupRequestDTO) {
        try {
            EmailLookupResponseDTO emailLookupResponseDTO = emailLookupService.findBynickNameAndRunningType(
                    emailLoookupRequestDTO.getNickName(), emailLoookupRequestDTO.getRunningType());
            return ResponseEntity.ok(emailLookupResponseDTO);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

    //* 비밀번호 재설정 요청
    @Operation(summary = "비밀번호 재설정 시 이메일로 인증코드를 보내는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 인증코드 전송 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid VerificationCodeRequestDTO request) {
        try {
            // 비밀번호 재설정 코드 생성 및 이메일 발송 로직 처리
            signupService.createPasswordResetCode(request.getEmail());
            return ResponseEntity.ok(SuccessCode.SUCCESS_RESET_CODE_SENT.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }
    //* 비밀번호 재설정 확인
    @Operation(summary = "비밀번호를 재설정하는 API 입니다. [담당자] : 정현아", description = "인증코드 및 재설정 할 비밀번호를 같이 입력해주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<?> confirmResetPassword(@RequestBody @Valid VerificationCodeConfirmDTO confirmDTO) {
        String result = signupService.validatePasswordResetCode(confirmDTO.getCode());
        if (!result.equals("valid")) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_RESET_INVALID.getMessage());
        }
        try {
            // 비밀번호 재설정
            signupService.resetPassword(confirmDTO.getCode(), confirmDTO.getNewPassword());
            return ResponseEntity.ok(SuccessCode.SUCCESS_PASSWORD_RESET.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_RESET_INVALID.getMessage());
        }
    }
    //* 비밀번호 확인
    @Operation(summary = "설정 -> 비밀번호 변경 시 확인하는 API 입니다. [담당자] : 정현아")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 확인 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/confirm-password")
    public ResponseEntity<?> confirmPassword(@RequestHeader("Authorization") String token,
                                             @RequestBody PasswordConfirmDTO passwordConfirmDTO) {
        try {
            signupService.confirmPassword(token, passwordConfirmDTO);
            return ResponseEntity.ok(SuccessCode.SUCCESS_PASSWORD_CONFIRM.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_MISMATCH.getMessage());
        }
    }
}
