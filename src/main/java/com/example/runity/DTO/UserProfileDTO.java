package com.example.runity.DTO;

import com.example.runity.enums.RunningType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Double height;
    private Double weight;
    private RunningType run_level;
}
