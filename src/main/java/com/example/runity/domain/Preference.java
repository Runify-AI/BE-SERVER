package com.example.runity.domain;

import com.example.runity.enums.PreferenceAvoid;
import com.example.runity.enums.PreferenceEtc;
import com.example.runity.enums.PreferencePlace;
import com.example.runity.enums.PreferenceRoute;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "preferences")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Preference {
    // 선호도 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;
    // 유저 ID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false, unique = true)
    private User user;
    // 선호 장소
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_places", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "place")
    @Enumerated(EnumType.STRING)
    private List<PreferencePlace> preferencePlaces;
    // 선호 경로
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_routes", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "route")
    @Enumerated(EnumType.STRING)
    private List<PreferenceRoute> preferenceRoutes;
    // 피하고 싶은 경로
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_avoids", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "avoid")
    @Enumerated(EnumType.STRING)
    private List<PreferenceAvoid> preferenceAvoids;
    // 기타
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preference_etcs", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "etc")
    @Enumerated(EnumType.STRING)
    private List<PreferenceEtc> preferenceEtcs;
    // 생성일
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    // 수정일
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public void updatePreference(
            List<PreferencePlace> places,
            List<PreferenceRoute> routes,
            List<PreferenceAvoid> avoids,
            List<PreferenceEtc> etcs
    ){
        this.preferencePlaces = places;
        this.preferenceRoutes = routes;
        this.preferenceAvoids = avoids;
        this.preferenceEtcs = etcs;
    }
}
