package com.example.runity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST 잘못된 요청
    INVALID_VERIFICATION_CODE(400, "유효하지 않은 인증 코드이거나 만료된 코드입니다."),
    EMAIL_VERIFY_NEED(400, "이메일 인증이 필요합니다."),
    EMAIL_ALREADY_REGISTERED(400, "이미 가입된 이메일입니다."),
    EMAIL_VERIFY_FAIL(400, "이메일 인증에 실패했습니다."),
    PASSWORD_RESET_INVALID(400, "비밀번호 재설정 코드가 유효하지 않거나 만료된 코드입니다."),
    INVALID_UPDATE_REQUEST(400, "수정할 정보가 없거나 기존 값과 동일합니다."),
    INVALID_PLACE(400,"유효하지 않은 장소 값입니다."),
    INVALID_DAY(400,"유효하지 않은 요일 값입니다."),
    INVALID_TIME_FORMAT(400,"시간 형식이 잘못되었습니다. (HH:mm)"),
    INVALID_ROUTE_PARAMETER(400,"경로 요청 값이 올바르지 않습니다."),
    INVALID_ROUTINE_PARAMETER(400,"루틴 요청 값이 올바르지 않습니다."),
    INVALID_PREFERENCE_ENUM_VALUE(400,"유효하지 않은 선호도 값입니다."),
    DUPLICATE_PREFERENCE_EXISTS(400,"이미 등록된 선호도 정보가 존재합니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(404, "존재하지 않는 회원 ID 입니다."),
    VERIFICATION_CODE_NOT_FOUND(404, "인증 코드가 존재하지 않습니다."),
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    ROUTE_NOT_FOUND(404,"경로를 찾을 수 없습니다."),
    ROUTINE_NOT_FOUND(404, "루틴 정보를 찾을 수 없습니다."),
    PREFERENCE_NOT_FOUND(404,"선호도 정보를 찾을 수 없습니다."),

    // 409 CONFLICT 중복된 리소스
    EMAIL_CONFLICT(409, "해당 이메일은 이미 등록되어 있습니다."),
    PREFERENCE_CONFLICT(409, "해당 유저의 선호도 정보가 이미 존재합니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    TOOMANY_REQUEST(500, "API 요청을 잠시 후 다시 시도해 주세요.");

    private final int status;
    private final String message;
}
