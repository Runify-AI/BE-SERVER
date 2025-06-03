package com.example.runity.DTO;

import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineRequestDTO {
    // 유저 ID
    @NotNull(message = "UserID cannot be null")
    private Long userId;
    // 장소
    @NotNull(message = "Place cannot be null")
    private Place place;
    // 시간
    @NotNull(message = "Time cannot be null")
    private String time;
    // 요일
    @NotNull(message = "Day list cannot be null")
    private List<Day> day;
}
