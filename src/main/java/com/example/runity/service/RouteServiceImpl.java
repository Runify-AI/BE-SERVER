package com.example.runity.service;

import com.example.runity.DTO.RouteRequestDTO;
import com.example.runity.DTO.RouteChoiceRequestDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    @Override
    public Route createRoute(RouteRequestDTO routeRequestDTO){
        User user = userRepository.findById(routeRequestDTO.getUserId())
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
                .build();

        RouteChoice routeChoice = RouteChoice.builder()
                .usePublicTransport(choiceDTO.getUsePublicTransport())
                .preferSafePath(choiceDTO.getPreferSafePath())
                .avoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas())
                .route(route)
                .build();

        route.getRouteChoices().add(routeChoice);

        return routeRepository.save(route);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRouteByUser(Long userId){
        return routeRepository.findByUserUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Route getRouteById(Long routeId){
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));
    }

    @Override
    public void updateRoute(Long routeId, RouteRequestDTO routeRequestDTO){
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));

        RouteChoiceRequestDTO choiceDTO = routeRequestDTO.getRouteChoiceRequestDTO();
        if (choiceDTO == null) {
            throw new CustomException(ErrorCode.INVALID_ROUTE_PARAMETER, ErrorCode.INVALID_ROUTE_PARAMETER.getMessage());
        }

        route.update(
                routeRequestDTO.getStartPoint(),
                routeRequestDTO.getEndPoint(),
                routeRequestDTO.getEstimatedTime(),
                routeRequestDTO.getDistance()
        );

        List<RouteChoice> routeChoices = route.getRouteChoices();
        if (routeChoices == null) {
            routeChoices = new ArrayList<>();
            route.setRouteChoices(routeChoices);
        }

        if (!routeChoices.isEmpty()) {
            RouteChoice routeChoice = routeChoices.get(routeChoices.size() - 1);
            routeChoice.setUsePublicTransport(choiceDTO.getUsePublicTransport());
            routeChoice.setPreferSafePath(choiceDTO.getPreferSafePath());
            routeChoice.setAvoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas());
        } else {
            RouteChoice newChoice = RouteChoice.builder()
                    .usePublicTransport(choiceDTO.getUsePublicTransport())
                    .preferSafePath(choiceDTO.getPreferSafePath())
                    .avoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas())
                    .route(route)
                    .build();
            routeChoices.add(newChoice);
        }

        routeRepository.save(route);
    }

    @Override
    public void deleteRoute(Long routeId){
        if(!routeRepository.existsById(routeId)){
            throw new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage());
        }
        routeRepository.deleteById(routeId);
    }
}
