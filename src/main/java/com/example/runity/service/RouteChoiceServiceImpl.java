package com.example.runity.service;

import com.example.runity.domain.Route;
import com.example.runity.domain.RouteChoice;
import com.example.runity.DTO.RouteChoiceRequestDTO;
import com.example.runity.repository.RouteChoiceRepository;
import com.example.runity.repository.RouteRepository;
import com.example.runity.service.RouteChoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteChoiceServiceImpl implements RouteChoiceService {
    private final RouteChoiceRepository routeChoiceRepository;
    private final RouteRepository routeRepository;

    @Override
    public RouteChoice saveChoice(Long routeId, RouteChoiceRequestDTO routeChoiceRequestDTO) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("경로를 찾을 수 없습니다."));

        RouteChoice routeChoice = RouteChoice.builder()
                .route(route)
                .usePublicTransport(routeChoiceRequestDTO.getUsePublicTransport())
                .preferSafePath(routeChoiceRequestDTO.getPreferSafePath())
                .avoidCrowdedAreas(routeChoiceRequestDTO.getAvoidCrowdedAreas())
                .build();

        return  routeChoiceRepository.save(routeChoice);
    }

    @Override
    public List<RouteChoice> getChoiceByRouteId(Long routeId){
        return routeChoiceRepository.findAllByRouteRouteId(routeId);
    }
}
