package com.example.runity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // 200
    SUCCESS_EMAIL_SENT(200, "인증 이메일이 성공적으로 발송되었습니다."),
    EMAIL_VERIFIED(200, "이메일 인증이 완료되었습니다."),
    SUCCESS_SIGNUP(200, "회원가입을 성공했습니다."),
    SUCCESS_LOGIN(200, "로그인을 성공했습니다."),
    SUCCESS_RESET_CODE_SENT(200, "비밀번호 재설정 코드가 발송되었습니다."),
    SUCCESS_PASSWORD_RESET(200, "비밀번호가 성공적으로 재설정되었습니다."),
    SUCCESS_PASSWORD_CONFIRM(200, "비밀번호 확인이 완료되었습니다."),
    SUCCESS_ROUTINE_UPDATE(200, "루틴 수정이 완료되었습니다."),
    SUCCESS_ROUTINE_LIST(200,"루틴 조회가 완료되었습니다."),
    SUCCESS_ROUTE_LIST(200,"경로 목록 조회가 완료되었습니다."),
    SUCCESS_ROUTE_DETAIL(200, "단일 경로 조회가 완료되었습니다."),
    SUCCESS_ROUTE_UPDATE(200, "경로 수정이 완료되었습니다."),
    SUCCESS_PREFERENCE_GET(200,"선호도 조회가 완료되었습니다."),
    SUCCESS_PREFERENCE_UPDATE(200,"선호도 수정이 완료되었습니다."),
    SUCCESS_ROUTINE_DELETE(200,"루틴 삭제가 완료되었습니다."),
    SUCCESS_ROUTE_DELETE(200, "경로 삭제가 완료되었습니다."),
    SUCCESS_PREFERENCE_DELETE(200,"선호도 삭제가 완료되었습니다."),
    // 201
    SUCCESS_ROUTINE_CREATE(201,"루틴 생성이 완료되었습니다."),
    SUCCESS_ROUTE_CREATE(201, "경로 생성이 완료되었습니다."),
    SUCCESS_PREFERENCE_CREATE(201, "선호도 생생성이 완료되었습니다.");

    private final int status;
    private final String message;
}
