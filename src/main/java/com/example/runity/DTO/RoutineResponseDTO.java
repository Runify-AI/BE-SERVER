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
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponseDTO {
    private Long routineId;
    private Place place;
    private String time;
    private List<Day> day;
    private List<CoordinateDTO> coordinates;

    public static RoutineResponseDTO from(Routine routine) {
        if (routine == null) return null;

        return RoutineResponseDTO.builder()
                .routineId(routine.getRoutineId())
                .place(routine.getPlace())
                .time(routine.getTime() != null ? routine.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : null)
                .day(routine.getDay())
                .coordinates(routine.getCoordinates().stream()
                        .map(coord -> new CoordinateDTO(coord.getLatitude(), coord.getLongitude()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CoordinateDTO {
        private double latitude;
        private double longitude;
    }

    public RoutineResponseDTO(Routine routine) {
        this.routineId = routine.getRoutineId();
        this.place = routine.getPlace();
        this.time = routine.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.day = routine.getDay();
        this.coordinates = routine.getCoordinates().stream()
                .map(coord -> new CoordinateDTO(coord.getLatitude(), coord.getLongitude()))
                .collect(Collectors.toList());
    }
}
