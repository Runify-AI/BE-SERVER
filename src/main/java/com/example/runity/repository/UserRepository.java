package com.example.runity.repository;

import com.example.runity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{ // Email로 사용자의 정보 조회 및 저장하는 Repository
    Optional<User> findByUserId(Long userId);

    Optional<User> findByEmail(String email);
    // 이메일 존재 여부 확인 메서드
    boolean existsByEmail(String email);
}
