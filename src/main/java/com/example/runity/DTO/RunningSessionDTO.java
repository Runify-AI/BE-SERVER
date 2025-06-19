package com.example.runity.DTO;

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
