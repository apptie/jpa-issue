package com.jpa.issue.repository;

import com.jpa.issue.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("select r from Region r join fetch r.secondRegions, r.thirdRegions where r.id = :id")
    Region findRegionWithSecondAndThirdRegion(@Param("id") Long id);
}
