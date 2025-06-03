package com.example.runity.service;

import com.example.runity.DTO.UserProfileResponseDTO;
import com.example.runity.constants.ErrorCode;
import com.example.runity.domain.User;
import com.example.runity.error.CustomException;
import com.example.runity.repository.UserRepository;
import com.example.runity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Override
    public UserProfileResponseDTO getUserProfile(String token) {
        Long userId = jwtUtil.getUserId(token); // JWT에서 userId 추출
        User user = userRepository.findByUserId(userId) // 정보 조회
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        // DTO로 반환
        return UserProfileResponseDTO.builder()
                .name(user.getName())
                .nickName(user.getNickName())
                .build();
    }
    @Override
    @Transactional
    public UserProfileResponseDTO updateProfile(String token, UserProfileResponseDTO updateRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));

        String updatedName = updateRequestDTO.getName();
        String updatedNickName = updateRequestDTO.getNickName();

        // 기존 프로필이랑 변경 사항이 있는지 확인
        boolean isUpdated = false;

        User.UserBuilder userBuilder = user.toBuilder(); // 엔터티의 일부(이름, 닉네임)만 변경하도록

        if (updatedName != null && !updatedName.equals(user.getName())) { // 사용자의 이름을 수정하는 로직
            userBuilder.name(updatedName);
            isUpdated = true;
        }

        if (updatedNickName != null && !updatedNickName.equals(user.getNickName())) { // 사용자의 닉네임을 수정하는 로직
            userBuilder.nickName(updatedNickName);
            isUpdated = true;
        }

        if (!isUpdated) {
            return UserProfileResponseDTO.builder()
                    .name(user.getName())
                    .nickName(user.getNickName())
                    .build();
        }

        User updatedUser = userBuilder.build();
        userRepository.save(updatedUser);

        return UserProfileResponseDTO.builder()
                .name(updatedUser.getName())
                .nickName(updatedUser.getNickName())
                .build();
    }
}
