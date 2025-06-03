package com.example.runity.service;

import com.example.runity.DTO.CustomUserInfoDTO;
import com.example.runity.DTO.LoginRequestDTO;
import com.example.runity.constants.ErrorCode;
import com.example.runity.domain.User;
import com.example.runity.error.CustomException;
import com.example.runity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //** SpringBean에 등록한 passwordEncoder를 주입해주기

    @Override
    public User login(LoginRequestDTO loginRequestDTO){
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        // enabled 값이 false인 경우 로그인 불가
        if (!user.isEnabled()) {
            throw new CustomException(ErrorCode.EMAIL_VERIFY_NEED,ErrorCode.EMAIL_VERIFY_NEED.getMessage());

        }
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH,ErrorCode.PASSWORD_MISMATCH.getMessage());
        }
        return user;
    }
    @Override
    public CustomUserInfoDTO toCustomUserInfoDTO(User user){
        return CustomUserInfoDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
    @Override
    public User getUserById(Long userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }
}
