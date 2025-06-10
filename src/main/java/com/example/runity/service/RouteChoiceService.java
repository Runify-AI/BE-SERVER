package com.example.runity.service;

import com.example.runity.domain.Route;
import com.example.runity.domain.RouteChoice;
import com.example.runity.DTO.RouteChoiceRequestDTO;

import java.util.List;

public interface RouteChoiceService {
    // 특정 경로(routeId)에 대한 사용자 경로 선택 정보 저장
    RouteChoice saveChoice(Long routeId, RouteChoiceRequestDTO routeChoiceRequestDTO);
    // 특정 경로에 연관된 모든 경로 설정 목록 조회
    List<RouteChoice> getChoiceByRouteId(Long routeId);
}
