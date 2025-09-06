package com.example.runity.domain;

import com.example.runity.enums.RunningType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@NoArgsConstructor
public class User {
    // 유저 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    // 이메일
    @Column(unique = true, nullable = false)
    private String email;
    // 비밀번호
    @Column(nullable = false)
    private String password;
    // 이름
    @Column(nullable = false)
    private String name;
    // 닉네임
    @Column(nullable = false)
    private String nickName;
    // 프로필 사진 URL 또는 경로
    private String profile;
    // 이메일 인증 여부
    private boolean enabled = false;
    // 키
    @Column(nullable = false)
    private Double height;
    // 체중
    @Column(nullable = false)
    private Double weight;
    // 러닝 타입
    @Enumerated(EnumType.STRING)
    private RunningType runningType;
    // 비밀번호 변경 메서드
    public void setPassword(String password) {
        this.password = password;
    }
}
