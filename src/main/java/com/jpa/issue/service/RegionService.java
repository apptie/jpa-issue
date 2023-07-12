package com.jpa.issue.service;

import com.jpa.issue.entity.Region;
import com.jpa.issue.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public Long addFirstRegion(String name) {
        final Region firstRegion = new Region(name);

        regionRepository.save(firstRegion);

        return firstRegion.getId();
    }

    public Long addSecondRegion(String name, Long firstRegionId) {
        final Region firstRegion = regionRepository.findById(firstRegionId)
                .orElseThrow(() -> new RuntimeException("첫 번째 지역이 존재하지 않습니다."));

        final Region secondRegion = new Region(name);

        firstRegion.initSecondRegion(secondRegion);

        return secondRegion.getId();
    }

    public Long addThirdRegion(String name, Long secondRegionId) {
        final Region secondRegion = regionRepository.findById(secondRegionId)
                .orElseThrow(() -> new RuntimeException("두 번째 지역이 존재하지 않습니다."));

        final Region thirdRegion = new Region(name);

        secondRegion.initThirdRegion(thirdRegion);

        return thirdRegion.getId();
    }
}
