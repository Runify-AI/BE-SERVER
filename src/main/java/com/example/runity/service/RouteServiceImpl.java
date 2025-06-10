package com.example.runity.service;

import com.example.runity.DTO.RouteRequestDTO;
import com.example.runity.DTO.RouteChoiceRequestDTO;
import com.example.runity.domain.Route;
import com.example.runity.domain.RouteChoice;
import com.example.runity.domain.User;
import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    @Override
    public Route createRoute(RouteRequestDTO routeRequestDTO){
        User user = userRepository.findById(routeRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Route route = new Route();
        route.setUser(user);
        route.setStartPoint(routeRequestDTO.getStartPoint());
        route.setEndPoint(routeRequestDTO.getEndPoint());
        route.setEstimatedTime(routeRequestDTO.getEstimatedTime());
        route.setDistance(routeRequestDTO.getDistance());
        route.setCreatedAt(LocalDateTime.now());

        RouteChoiceRequestDTO choiceDTO = routeRequestDTO.getRouteChoiceRequestDTO();
        if (choiceDTO == null) {
            throw new IllegalArgumentException("경로 설정 정보가 누락되었습니다."); // routeChoiceRequestDTO
        }

        RouteChoice routeChoice = new RouteChoice();
        routeChoice.setUsePublicTransport(choiceDTO.getUsePublicTransport());
        routeChoice.setPreferSafePath(choiceDTO.getPreferSafePath());
        routeChoice.setAvoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas());
        routeChoice.setRoute(route);

        route.getRouteChoices().add(routeChoice);

        return routeRepository.save(route);
    }

    @Override
    public List<Route> getRouteByUser(Long userId){
        return routeRepository.findByUserUserId(userId);
    }

    @Override
    public Route getRouteById(Long routeId){
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경로가 존재하지 않습니다."));
    }

    @Override
    public void updateRoute(Long routeId, RouteRequestDTO routeRequestDTO){
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경로가 존재하지 않습니다."));
        route.update(
                routeRequestDTO.getStartPoint(),
                routeRequestDTO.getEndPoint(),
                routeRequestDTO.getEstimatedTime(),
                routeRequestDTO.getDistance()
        );

        RouteChoiceRequestDTO choiceDTO = routeRequestDTO.getRouteChoiceRequestDTO();
        if (choiceDTO == null) {
            throw new IllegalArgumentException("경로 설정 정보가 누락되었습니다."); // routeChoiceRequestDTO
        }

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
            RouteChoice newPref = new RouteChoice();
            newPref.setUsePublicTransport(choiceDTO.getUsePublicTransport());
            newPref.setPreferSafePath(choiceDTO.getPreferSafePath());
            newPref.setAvoidCrowdedAreas(choiceDTO.getAvoidCrowdedAreas());
            newPref.setRoute(route);
            routeChoices.add(newPref);
        }

        routeRepository.save(route);
    }

    @Override
    public void deleteRoute(Long routeId){
        if(!routeRepository.existsById(routeId)){
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        routeRepository.deleteById(routeId);
    }
}
