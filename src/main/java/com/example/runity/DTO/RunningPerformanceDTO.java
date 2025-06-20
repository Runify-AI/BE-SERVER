package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningPerformanceDTO {
    private UserProfileDTO user_profile;
    private List<UserFeedbackDTO> feedback_history;
    private float avg_pause_times;
    private List<Float> avg_pace_list;
    private List<Float> avg_speed_list;
    //private RouteInfoDTO route_info;
    private WeatherDTO environmental_context;
}