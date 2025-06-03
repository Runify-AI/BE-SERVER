package com.example.runity.service;

import com.example.runity.domain.User;
import com.example.runity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(Long userId) throws UsernameNotFoundException {
        // userId를 기반으로 사용자 정보 로드
        User user= userRepository.findByUserId(userId)
                .orElseThrow(()-> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),// 계정 활성화 여부
                true, // 계정 만료 여부
                true, // 자격 증명 만료 여부
                true, // 계정 잠금 여부
                Collections.emptyList() // 권한 목록 (필요 시 추가)
        );
    }
}
