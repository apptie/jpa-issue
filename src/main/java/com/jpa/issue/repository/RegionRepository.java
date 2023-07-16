package com.jpa.issue.repository;

import com.jpa.issue.entity.Region;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepository extends JpaRepository<Region, String> {

    @Query("select r from Region r where r.secondRegion.id is null")
    List<Region> findTotalRegionsByHierarchy();
}
