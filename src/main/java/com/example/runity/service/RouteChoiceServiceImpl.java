package com.example.runity.service;

import com.example.runity.constants.ErrorCode;
import com.example.runity.error.CustomException;
import com.example.runity.domain.Route;
import com.example.runity.domain.RouteChoice;
import com.example.runity.DTO.route.RouteChoiceRequestDTO;
import com.example.runity.repository.RouteChoiceRepository;
import com.example.runity.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteChoiceServiceImpl implements RouteChoiceService {

    private final RouteChoiceRepository routeChoiceRepository;
    private final RouteRepository routeRepository;

    @Override
    public RouteChoice saveChoice(Long routeId, RouteChoiceRequestDTO routeChoiceRequestDTO) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND, ErrorCode.ROUTE_NOT_FOUND.getMessage()));

        RouteChoice routeChoice = RouteChoice.builder()
                .route(route)
                .usePublicTransport(routeChoiceRequestDTO.getUsePublicTransport())
                .preferSafePath(routeChoiceRequestDTO.getPreferSafePath())
                .avoidCrowdedAreas(routeChoiceRequestDTO.getAvoidCrowdedAreas())
                .cycle(routeChoiceRequestDTO.getCycle())
                .build();

        return  routeChoiceRepository.save(routeChoice);
    }

    @Override
    public List<RouteChoice> getChoiceByRouteId(Long routeId){
        return routeChoiceRepository.findAllByRouteRouteId(routeId);
    }
}
