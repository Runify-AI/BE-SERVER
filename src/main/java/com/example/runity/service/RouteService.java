package com.example.runity.service;

import com.example.runity.DTO.RouteRequestDTO;
import com.example.runity.domain.Route;

import java.util.List;

public interface RouteService {
    // 경로 생성
    Route createRoute(RouteRequestDTO routeRequestDTO);
    // 유저 ID로 경로 리스트 조회
    List<Route> getRouteByUser(Long userId);
    // 경로 ID로 하나ㅢ 경로 조회
    Route getRouteById(Long routeId);
    // 경로 수정
    void updateRoute(Long routeId, RouteRequestDTO routeRequestDTO);
    // 경로 삭제
    void deleteRoute(Long routeId);
}
