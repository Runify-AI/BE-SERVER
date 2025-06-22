package com.example.runity.service;

import com.example.runity.DTO.RunningSettingResponseDTO;
import com.example.runity.DTO.route.RouteRequestDTO;
import com.example.runity.DTO.route.RouteResponseDTO;

import java.util.List;

public interface RouteService {

    // 경로 생성
    RouteResponseDTO createRoute(String token, RouteRequestDTO routeRequestDTO);
    // 유저 ID로 경로 리스트 조회
    List<RunningSettingResponseDTO> getRouteByUser(String token);
    // 경로 ID로 하나ㅢ 경로 조회
    RunningSettingResponseDTO getRouteById(Long routeId);
    // 경로 수정
    void updateRoute(String token, Long routeId, RouteRequestDTO routeRequestDTO);
    // 경로 삭제
    void deleteRoute(String token, Long routeId);
    // 패스 선택
    void selectPath(Long routeId, Long pathId);
}
