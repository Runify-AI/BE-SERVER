package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ReturnCodeDTO<T> {
    private int status;
    private String message;
    private Object data;
    private T data;

    public ReturnCodeDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ReturnCodeDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null; // data가 없을 경우
    }
}