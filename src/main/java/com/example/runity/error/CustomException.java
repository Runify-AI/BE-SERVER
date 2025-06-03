package com.example.runity.error;

import com.example.runity.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String message;
}
