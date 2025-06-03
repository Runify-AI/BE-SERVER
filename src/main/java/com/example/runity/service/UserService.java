package com.example.runity.service;

import com.example.runity.DTO.UserProfileResponseDTO;

public interface UserService {
    UserProfileResponseDTO getUserProfile(String token); // 토큰을 받아서 프로필 정보 조회
    UserProfileResponseDTO updateProfile(String token, UserProfileResponseDTO updateRequestDTO); // 토큰을 받아서 프로필 정보 수정
}
