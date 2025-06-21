package com.example.runity.service;

import com.example.runity.DTO.EmailLookupResponseDTO;
import com.example.runity.constants.ErrorCode;
import com.example.runity.enums.RunningType;
import com.example.runity.error.CustomException;
import com.example.runity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailLookupServiceImpl implements EmailLookupService {
    private final UserRepository  userRepository;

    @Override
    public EmailLookupResponseDTO findBynickNameAndRunningType (String nickName, RunningType runningType) {
        return userRepository.findBynickNameAndRunningType(nickName, runningType)
                .map(user -> new EmailLookupResponseDTO(user.getEmail()))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "해당 정보로 가입된 사용자가 없습니다."));
    }
}
