package com.example.runity.DTO.runningTS;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StaticsHistoryRequestDTO {
    List<StaticsHistoryDTO> history;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class StaticsHistoryDTO {
        double distance;
        int elapsedTime;
        double pace;
    }
}
