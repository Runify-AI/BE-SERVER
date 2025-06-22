package com.example.runity.repository;

import com.example.runity.domain.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface PathRepository extends JpaRepository<Path, Long> {
    public List<Path> findAllByRoute_RouteId(Long routeId);

    @Query("SELECT MAX(p.pathId) FROM Path p WHERE p.route.routeId = :routeId")
    Integer findMaxPathIdByRouteId(@Param("routeId") Long routeId);
}
