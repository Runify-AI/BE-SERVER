package com.example.runity.service;

import com.example.runity.DTO.route.RouteResponseDTO;
import com.example.runity.DTO.route.RoutineResponseDTO;
import com.example.runity.util.JwtUtil;
import com.example.runity.DTO.route.RouteRequestDTO;
import com.example.runity.DTO.route.RouteChoiceRequestDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
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

        LocalDateTime estimatedTime = routeRequestDTO.getEstimatedTime();


        Route route = Route.builder()
                .user(user)
                .startPoint(routeRequestDTO.getStartPoint())
                .endPoint(routeRequestDTO.getEndPoint())
                .estimatedTime(routeRequestDTO.getEstimatedTime())
                .distance(routeRequestDTO.getDistance())
                .createdAt(LocalDateTime.now())
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
    public List<RouteResponseDTO> getRouteByUser(String token) {
        Long userId = jwtUtil.getUserId(token);
        List<Route> routes = routeRepository.findByUserUserId(userId);
        return routes.stream()
                .map(route -> new RouteResponseDTO(route, RoutineResponseDTO.from(route.getRoutine()), route.getRouteChoices()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RouteResponseDTO getRouteById(Long routeId){
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));

        return new RouteResponseDTO(route, RoutineResponseDTO.from(route.getRoutine()), route.getRouteChoices());
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

        LocalDateTime estimatedTime = routeRequestDTO.getEstimatedTime();

        route.update(
                routeRequestDTO.getStartPoint(),
                routeRequestDTO.getEndPoint(),
                routeRequestDTO.getEstimatedTime(),
                routeRequestDTO.getDistance()
        );

        Set<Route.Coordinate> coordinateSet = new HashSet<>();
        if (routeRequestDTO.getCoordinates() != null) {
            for (RouteRequestDTO.CoordinateDTO coordinateDTO : routeRequestDTO.getCoordinates()) {
                coordinateSet.add(new Route.Coordinate(coordinateDTO.getLatitude(), coordinateDTO.getLongitude()));
            }
        }

        route.getCoordinates().clear();
        route.getCoordinates().addAll(coordinateSet);

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
}
