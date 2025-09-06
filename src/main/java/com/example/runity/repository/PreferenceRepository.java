package com.example.runity.repository;

import com.example.runity.domain.Preference;
import com.example.runity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByUser(User user); // 유저의 선호도 설정을 조회
    void deleteByUser(User user); // 유저의 선호도 설정을 삭제
    boolean existsByUser(User user);
}
