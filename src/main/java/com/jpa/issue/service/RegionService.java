package com.jpa.issue.service;

import com.jpa.issue.entity.Region;
import com.jpa.issue.repository.RegionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegionService {

    private final RegionProcessor regionProcessor;
    private final RegionRepository regionRepository;

    public RegionService(RegionProcessor regionProcessor, RegionRepository regionRepository) {
        this.regionProcessor = regionProcessor;
        this.regionRepository = regionRepository;
    }

    public void initializationRegions() {
        final List<Region> regions = regionProcessor.requestTotalRegions();

        regionRepository.saveAll(regions);
    }

    @Transactional(readOnly = true)
    public List<Region> findAllRegions() {
        return regionRepository.findTotalRegionsByHierarchy();
    }
}
