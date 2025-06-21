package com.example.runity.repository;

import com.example.runity.domain.PathFeature;
import com.example.runity.enums.FeatureType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PathFeatureRepository extends JpaRepository<PathFeature, Long> {
    List<PathFeature> findByPath_Id(Long pathId);
    List<PathFeature> findByPath_IdAndFeatureType(Long pathId, FeatureType type);
}
