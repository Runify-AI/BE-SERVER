package com.example.runity.service;

import com.example.runity.DTO.CustomUserInfoDTO;
import com.example.runity.DTO.LoginRequestDTO;
import com.example.runity.domain.User;

public interface LoginService {
    User login(LoginRequestDTO loginRequestDTO);
    CustomUserInfoDTO toCustomUserInfoDTO(User user);
    User getUserById(Long userId);
}
