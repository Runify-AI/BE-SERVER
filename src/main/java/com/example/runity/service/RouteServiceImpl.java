package com.example.runity.service;

import com.example.runity.DTO.RunningSettingResponseDTO;
import com.example.runity.DTO.route.*;
import com.example.runity.domain.*;
import com.example.runity.repository.PathRepository;
import com.example.runity.repository.RoutineRepository;
import com.example.runity.util.JwtUtil;
import com.example.runity.constants.ErrorCode;
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
    @Transactional(readOnly = false)
    public List<RunningSettingResponseDTO> getRouteByUser(String token) {
        Long userId = jwtUtil.getUserId(token);
        List<Route> routes = routeRepository.findByUserUserIdAndCompletedFalse(userId);

        return routes.stream()
                .map(route -> {
                    List<RecommendedPathsDTO> paths = List.of(); // 추천 경로 리스트 초기화

                    // 추천 경로가 아직 선택되지 않은 경우
                    if (route.getSelectedPathId() == null ) {
                        try {
                            // AI 추천 요청용 DTO 생성
                            RecommendationRequestDTO request = recommendationService.generateRecommendations(token, route.getRouteId());
                            String startAddr = route.getStartPoint();
                            String endAddr = route.getEndPoint();

                            // AI 서버에 추천 요청
                            RecommendationResponseDTO recommendedPaths = recommendationService.generateRecommendation(startAddr, endAddr, request);

                            if (recommendedPaths.getPaths() == null) {
                                System.out.println("AI 추천 결과가 null입니다.");
                            } else {
                                System.out.println("AI 추천 결과가 성공적으로 수신되었습니다.");
                            }

                            // 추천 결과 저장
                            recommendationService.saveRecommendationResults(route.getRouteId(), recommendedPaths);

                            // 추천 결과 저장 후 route 재조회
                            route = routeRepository.findById(route.getRouteId())
                                    .orElseThrow(() -> new RuntimeException("Route not found after saving recommendations."));

                            // 추천 결과 리스트 구성
                            paths = recommendedPaths.getPaths();

                        } catch (Exception e) {
                            System.out.println(String.format("AI 추천 처리 중 오류 발생 (routeId: %d)", route.getRouteId()));
                            System.out.println("오류 메시지: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        // 이미 저장된 추천 경로 불러오기
                        paths = pathRepository.findById(route.getSelectedPathId()).stream()
                                .map(Path::toRecommendationDTO)
                                .collect(Collectors.toList());
                    }

                    Routine routine = route.getRoutine();

                    // 응답 DTO 구성
                    return RunningSettingResponseDTO.builder()
                            .routeId(route.getRouteId())
                            .routineResponseDTO(RoutineResponseDTO.from(routine))
                            .completed(route.isCompleted())
                            .selectedPath(route.getSelectedPathId())
                            .paths(paths)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false)
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

        route.update(
                routeRequestDTO.getStartPoint(),
                routeRequestDTO.getEndPoint()
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

        if (!route.getUser().getUserId().equals(userId) || route.isCompleted()) {
            throw new CustomException(ErrorCode.INVALID_ROUTE_PARAMETER, ErrorCode.INVALID_ROUTE_PARAMETER.getMessage());
        }
        routeRepository.deleteById(routeId);
    }

    private RunningSettingResponseDTO buildRunningSettingResponseDTO(Route route) {
        RoutineResponseDTO routineDTO = RoutineResponseDTO.from(route.getRoutine());

        List<RecommendedPathsDTO> recommendations;

        Long selectedPathId = route.getSelectedPathId();
        boolean completed = route.isCompleted();

        if (selectedPathId != null) {
            // 선택된 Path만 조회
            recommendations = pathRepository.findById(selectedPathId)
                    .map(Path::toRecommendationDTO)
                    .map(List::of)
                    .orElse(List.of()); // 선택된 경로가 없을 경우 빈 리스트 반환
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

        // path가 해당 route에 속하는지 검증
        if (!path.getRoute().getRouteId().equals(routeId)) {
            throw new CustomException(ErrorCode.PATH_ROUTE_MISMATCH, "해당 path는 지정된 route에 속하지 않습니다.");
        }

        // 검증 완료 → 선택된 경로로 설정
        route.setSelectedPathId(pathId);
    }

}
