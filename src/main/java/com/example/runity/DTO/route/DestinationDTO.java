package com.example.runity.DTO.route;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 도착지의 위도, 경도, 이름을 저장하는 복합 객체.
 * JPA @Embeddable로 사용하여 Routine 테이블의 컬럼에 포함됩니다.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DestinationDTO {

    // 위도 (Latitude)
    @Column(nullable = false, name = "dest_latitude")
    private Double latitude;

    // 경도 (Longitude)
    @Column(nullable = false, name = "dest_longitude")
    private Double longitude;

    // 도착지 이름
    @Column(nullable = false, name = "dest_name")
    private String name;
}