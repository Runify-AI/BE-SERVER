package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnCodeDTO {
    private int status;
    private String message;
    private Object data;

    public ReturnCodeDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
