package com.example.runity.error;

import com.example.runity.constants.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage()); // 에러 메시지를 기본으로 사용
    this.errorCode = errorCode;
  }

  public CustomException(ErrorCode errorCode, String customMessage) {
    super(customMessage); // 필요 시 커스텀 메시지 허용
    this.errorCode = errorCode;
  }
}
