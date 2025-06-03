package com.example.runity.error;

import com.example.runity.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
