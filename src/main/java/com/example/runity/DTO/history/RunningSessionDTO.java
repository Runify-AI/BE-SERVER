package com.example.runity.DTO.history;

import com.example.runity.DTO.route.RunningSettingsResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RunningSessionDTO {
    private RunningSettingsResponse runningSettingsResponse;
    private RunningHistoryDTO runningHistoryDTO;
}
