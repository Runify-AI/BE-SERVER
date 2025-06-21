package com.example.runity.DTO;

import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    // 도착지
    @NotNull
    @Size(min = 1, max = 100, message = "도착지는 1~100자 이하로 입력해주세요.")
    private String destination;
    // 시간
    @NotNull
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "시간 형식은 HH:mm 형식이어야 합니다.")
    private String time;
    // 요일
    @NotNull
    @Size(min = 1, message = "최소 하나 이상의 요일을 선택해야 합니다.")
    private List<Day> day;
}
