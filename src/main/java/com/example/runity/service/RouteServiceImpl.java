package com.example.runity.service;

import com.example.runity.DTO.RunningSettingResponseDTO;
import com.example.runity.DTO.route.*;
import com.example.runity.domain.Path;
import com.example.runity.repository.PathRepository;
import com.example.runity.repository.RoutineRepository;
import com.example.runity.util.JwtUtil;
import com.example.runity.constants.ErrorCode;
import com.example.runity.domain.Route;
import com.example.runity.domain.RouteChoice;
import com.example.runity.domain.User;
import com.example.runity.error.CustomException;
import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final PathRepository pathRepository;
    private final RoutineRepository routineRepository;
    private final RecommendationService recommendationService;
    private final JwtUtil jwtUtil;

    @Override
    public RouteResponseDTO createRoute(String token, RouteRequestDTO routeRequestDTO){
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        RouteChoiceRequestDTO choiceDTO = routeRequestDTO.getRouteChoiceRequestDTO();
        if (choiceDTO == null) {
            throw new CustomException(ErrorCode.INVALID_ROUTE_PARAMETER, ErrorCode.INVALID_ROUTE_PARAMETER.getMessage());
        }


        Route route = Route.builder()
                .user(user)
                .startPoint(routeRequestDTO.getStartPoint())
                .endPoint(routeRequestDTO.getEndPoint())
                .estimatedTime(routeRequestDTO.getEstimatedTime())
                .distance(routeRequestDTO.getDistance())
                .createdAt(LocalDateTime.now())
                .routine(routineRepository.findByRoutineId(routeRequestDTO.getRoutineId()))
                .build();

        /*
        Set<Route.Coordinate> coordinateSet = new HashSet<>();
        if (routeRequestDTO.getCoordinates() != null) {
            for (RouteRequestDTO.CoordinateDTO coordinateDTO : routeRequestDTO.getCoordinates()) {
                coordinateSet.add(new Route.Coordinate(coordinateDTO.getLatitude(), coordinateDTO.getLongitude()));
            }
        }

        route.getCoordinates().addAll(coordinateSet);

         */

        RouteChoice routeChoice = RouteChoice.builder()
                .usePublicTransport(choiceDTO.getUsePublicTransport())
                .preferSafePath(choiceDTO.getPreferSafePath())
                .avoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas())
                .cycle(choiceDTO.getCycle())
                .route(route)
                .build();

        route.getRouteChoices().add(routeChoice);
        
        Route savedRoute = routeRepository.save(route);
        RoutineResponseDTO routineResponseDTO = RoutineResponseDTO.from(savedRoute.getRoutine());

        return new RouteResponseDTO(savedRoute, routineResponseDTO, savedRoute.getRouteChoices());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RunningSettingResponseDTO> getRouteByUser(String token) {
        Long userId = jwtUtil.getUserId(token);
        List<Route> routes = routeRepository.findByUserUserId(userId);

        return routes.stream()
                .map(route -> {
                    // selectedPathId가 null이면 AI 추천 경로 생성 및 저장
                    if (route.getSelectedPathId() == null) {
                        try {
                            // [1] 추천 요청 데이터 구성
                            RecommendationRequestDTO request = recommendationService.generateRecommendations(token, route.getRouteId());

                            // [2] AI 호출
                            String startAddr = route.getStartPoint();
                            String endAddr = route.getEndPoint();
                            List<RecommendationResponseDTO> recommendedPaths = recommendationService.generateRecommendation(startAddr, endAddr, request);

                            // [3] 추천 결과 DB 저장 및 Route에 selectedPathId 설정
                            recommendationService.saveRecommendationResults(route.getRouteId(), recommendedPaths);

                            // → route 객체가 변경되었으므로 최신 selectedPathId 반영 위해 새로 조회
                            route = routeRepository.findById(route.getRouteId())
                                    .orElseThrow(() -> new RuntimeException("Route not found after saving recommendations."));

                        } catch (Exception e) {
                            // 로그만 찍고 다음 route 계속 처리
                            System.out.println(String.format("AI 추천 처리 중 오류 발생 (routeId: %d)", route.getRouteId()));
                            System.out.println("오류 메시지: " + e.getMessage());
                            e.printStackTrace();  // 콘솔에 전체 스택 트레이스 출력
                        }
                    }

                    // [4] DTO 변환
                    return buildRunningSettingResponseDTO(route);
                })
                .collect(Collectors.toList());

    }


    @Override
    @Transactional(readOnly = true)
    public RunningSettingResponseDTO getRouteById(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));

        return buildRunningSettingResponseDTO(route);
    }

    @Override
    public void updateRoute(String token,Long routeId, RouteRequestDTO routeRequestDTO){
        Long userId = jwtUtil.getUserId(token);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));

        RouteChoiceRequestDTO choiceDTO = routeRequestDTO.getRouteChoiceRequestDTO();
        if (choiceDTO == null) {
            throw new CustomException(ErrorCode.INVALID_ROUTE_PARAMETER, ErrorCode.INVALID_ROUTE_PARAMETER.getMessage());
        }

        LocalTime estimatedTime = routeRequestDTO.getEstimatedTime();

        route.update(
                routeRequestDTO.getStartPoint(),
                routeRequestDTO.getEndPoint(),
                routeRequestDTO.getEstimatedTime(),
                routeRequestDTO.getDistance()
        );

        List<RouteChoice> routeChoices = route.getRouteChoices();

        if (!routeChoices.isEmpty()) {
            RouteChoice routeChoice = routeChoices.get(routeChoices.size() - 1);
            routeChoice.setUsePublicTransport(choiceDTO.getUsePublicTransport());
            routeChoice.setPreferSafePath(choiceDTO.getPreferSafePath());
            routeChoice.setAvoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas());
            routeChoice.setCycle(choiceDTO.getCycle());
        } else {
            RouteChoice newChoice = RouteChoice.builder()
                    .usePublicTransport(choiceDTO.getUsePublicTransport())
                    .preferSafePath(choiceDTO.getPreferSafePath())
                    .avoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas())
                    .cycle(choiceDTO.getCycle())
                    .route(route)
                    .build();
            routeChoices.add(newChoice);
        }

        routeRepository.save(route);
    }

    @Override
    public void deleteRoute(String token, Long routeId){
        Long userId = jwtUtil.getUserId(token);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));

        if (!route.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.INVALID_ROUTE_PARAMETER, ErrorCode.INVALID_ROUTE_PARAMETER.getMessage());
        }
        routeRepository.deleteById(routeId);
    }

    private RunningSettingResponseDTO buildRunningSettingResponseDTO(Route route) {
        RoutineResponseDTO routineDTO = RoutineResponseDTO.from(route.getRoutine());

        List<RecommendationResponseDTO> recommendations;

        Long selectedPathId = route.getSelectedPathId();
        boolean completed = route.isCompleted();

        if (selectedPathId != null) {
            // 선택된 Path만 조회
            recommendations = pathRepository.findById(selectedPathId)
                    .map(Path::toRecommendationDTO)
                    .map(List::of)
                    .orElse(List.of()); // 선택된 경로가 없을 경우 빈 리스트
        } else {
            // 전체 추천 경로 조회
            recommendations = pathRepository.findAllByRoute_RouteId(route.getRouteId())
                    .stream()
                    .map(Path::toRecommendationDTO)
                    .toList();
        }

        return RunningSettingResponseDTO.builder()
                .routeId(route.getRouteId())
                .routineResponseDTO(routineDTO)
                .completed(completed)
                .selectedPath(selectedPathId)
                .paths(recommendations)
                .build();
    }


    @Override
    @Transactional
    public void selectPath(Long routeId, Long pathId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, "해당 경로를 찾을 수 없습니다."));

        Path path = pathRepository.findById(pathId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATH_NOT_FOUND, "해당 추천 경로를 찾을 수 없습니다."));

        // path가 route에 속해 있는지 확인
        if (!path.getRoute().getRouteId().equals(routeId)) {
            throw new CustomException(ErrorCode.PATH_ROUTE_MISMATCH, "해당 path는 지정된 route에 속하지 않습니다.");
        }

        // 검증 통과 → route의 selectedPathId 갱신
        route.setSelectedPathId(pathId);
    }

}
