package com.jpa.issue.repository;

import com.jpa.issue.entity.Region;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("select r from Region r where r.firstRegion.id is null and r.secondRegion.id is null")
    List<Region> findTotalRegionsByHierarchy();

    @Query("select r from Region r where r.firstRegion.id is null")
    List<Region> findFirstRegion();

    @Query("select r from Region r where r.firstRegion.id = :id and r.secondRegion.id is null")
    List<Region> findSecondRegion(@Param("id") Long id);

    @Query("select r from Region r where r.firstRegion.id = :firstRegionId and r.secondRegion.id = :secondRegionId")
    List<Region> findThirdRegion(
            @Param("firstRegionId") Long firstRegionId,
            @Param("secondRegionId") Long secondRegionId
    );
}
