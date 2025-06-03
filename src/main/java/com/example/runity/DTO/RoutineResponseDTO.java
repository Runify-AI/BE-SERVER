package com.example.runity.DTO;

import com.example.runity.domain.Routine;
import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponseDTO {
    private Long routineId;
    private Place place;
    private String time;
    private List<Day> day;

    public RoutineResponseDTO(Routine routine) {
        this.routineId = routine.getRoutineId();
        this.place = routine.getPlace();
        this.time = routine.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.day = routine.getDay();
    }
}
