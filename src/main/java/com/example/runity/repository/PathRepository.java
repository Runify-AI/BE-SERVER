package com.example.runity.repository;

import com.example.runity.domain.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface PathRepository extends JpaRepository<Path, Long> {
    public List<Path> findAllByRoute_RouteId(Long routeId);
}
