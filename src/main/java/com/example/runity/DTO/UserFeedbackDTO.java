package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFeedbackDTO {
    private LocalDate date;
    private String feedback;
    private int score;
}