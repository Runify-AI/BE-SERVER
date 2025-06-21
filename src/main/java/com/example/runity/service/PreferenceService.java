package com.example.runity.service;

import com.example.runity.DTO.PreferenceRequestDTO;
import com.example.runity.DTO.PreferenceResponseDTO;

public interface PreferenceService {

    // 선호도 생성
    void createPreference(String token, PreferenceRequestDTO preferenceRequestDTO);
    // 선호도 조회
    PreferenceResponseDTO getPreference(String token);
    // 선호도 수정
    void updatePreference(String token, PreferenceRequestDTO preferenceRequestDTO);
    // 선호도 삭제
    void deletePreference(String token);
}
