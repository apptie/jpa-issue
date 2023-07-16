package com.jpa.issue.service;

import com.jpa.issue.entity.Region;
import com.jpa.issue.repository.RegionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RegionService {

    private final RegionProcessor regionProcessor;
    private final RegionRepository regionRepository;

    public RegionService(RegionProcessor regionProcessor, RegionRepository regionRepository) {
        this.regionProcessor = regionProcessor;
        this.regionRepository = regionRepository;
    }

    @Transactional
    public void initializationRegions() {
        final List<Region> regions = regionProcessor.requestTotalRegions();

        regionRepository.saveAll(regions);
    }

    public List<Region> findAllRegions() {
       return regionRepository.findTotalRegionsByHierarchy();
    }

    public List<Region> findFirstRegions() {
        return regionRepository.findFirstRegion();
    }

    public List<Region> findSecondRegions(Long firstRegionId) {
        return regionRepository.findSecondRegion(firstRegionId);
    }

    public List<Region> findThirdRegions(Long firstRegionId, Long secondRegionId) {
        return regionRepository.findThirdRegion(firstRegionId, secondRegionId);
    }
}
