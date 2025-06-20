package com.example.runity.DTO;

import com.example.runity.domain.Routine;
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
    // 장소
    @NotNull
    private Place place;
    // 시간
    @NotNull
    private String time;
    // 요일
    @NotNull
    private List<Day> day;
    // 좌표 리스트
    private List<CoordinateDTO> coordinates;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CoordinateDTO {
        private double latitude;
        private double longitude;
    }
}
