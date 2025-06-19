package com.example.runity.repository;

import com.example.runity.domain.RouteCoordinate;
import com.example.runity.domain.RouteCoordinateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteCoordinateRepository extends JpaRepository<RouteCoordinate, RouteCoordinateId> {

    List<RouteCoordinate> findByRoute_RouteId(Long routeId);  // 특정 경로의 좌표 리스트 조회

}
