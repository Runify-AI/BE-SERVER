package com.example.runity.DTO;

import com.example.runity.enums.RunningType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailLoookupRequestDTO {
    @NotBlank
    private String nickName;
    @NotNull
    private RunningType runningType;
}
