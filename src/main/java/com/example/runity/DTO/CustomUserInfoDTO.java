package com.example.runity.DTO;

import com.example.runity.enums.RunningType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserInfoDTO {
    private Long userId;
    private String email;
    private String name;
    private Double height;
    private Double weight;
    private RunningType runningType;
}