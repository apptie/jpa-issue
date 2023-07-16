package com.jpa.issue.service;

import com.jpa.issue.dto.RegionResponse;
import com.jpa.issue.entity.Region;
import com.jpa.issue.repository.RegionRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RegionProcessor regionProcessor;
    private final RegionRepository regionRepository;

    public RegionService(RegionProcessor regionProcessor, RegionRepository regionRepository) {
        this.regionProcessor = regionProcessor;
        this.regionRepository = regionRepository;
    }

    public void initializationRegions() {
        log.info("start initializationRegions");

        final String accessToken = regionProcessor.authentication();
        final List<RegionResponse> firstRegionResponses = regionProcessor.requestFirstRegions(accessToken);
        final ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (RegionResponse firstRegionResponse : firstRegionResponses) {
            executorService.submit(() -> init(accessToken, firstRegionResponse));
        }
        executorService.shutdown();
    }

    private void init(String accessToken, RegionResponse firstRegionResponse) {
        final Region firstRegion = regionProcessor.requestFullRegionsByFirstRegion(accessToken, firstRegionResponse);

        regionRepository.save(firstRegion);
    }
}
