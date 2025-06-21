package com.example.runity.error;

import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.constants.ErrorCode;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler { //** 예외 처리에 관한 Handler
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ReturnCodeDTO> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ReturnCodeDTO(ErrorCode.USER_NOT_FOUND.getStatus(), ex.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ReturnCodeDTO> handleJwtException(JwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ReturnCodeDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ReturnCodeDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ReturnCodeDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    // 필요한 다른 예외 처리 추가
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ReturnCodeDTO> handleCustomException(CustomException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(new ReturnCodeDTO(errorCode.getStatus(),ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ReturnCodeDTO> handleServerException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ReturnCodeDTO(
                        ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /*
    public ResponseEntity<Map<String,String>> dtoValidation(final MethodArgumentNotValidException e){
        Map<String ,String> errors=new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError) error).getField();
            String errorMessage=error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

     */
    public ResponseEntity<ReturnCodeDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(); // 첫 번째 에러만 추출

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ReturnCodeDTO(400, errorMessage));
    }
}
