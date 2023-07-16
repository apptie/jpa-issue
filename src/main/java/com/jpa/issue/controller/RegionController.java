package com.jpa.issue.controller;

import com.jpa.issue.dto.ReadRegionResponse;
import com.jpa.issue.entity.Region;
import com.jpa.issue.service.RegionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    @Cacheable(cacheNames = {"regionsCache"})
    public ResponseEntity<List<ReadRegionResponse>> findAll() {
        final List<Region> allRegions = regionService.findAllRegions();
        final List<ReadRegionResponse> readRegionResponses = new ArrayList<>();

        for (Region firstRegion : allRegions) {
            final List<ReadRegionResponse> secondRegionResponses = new ArrayList<>();
            final List<Region> secondRegions = firstRegion.getSecondRegions();

            for (Region secondRegion : secondRegions) {
                final List<ReadRegionResponse> thirdRegionResponses = new ArrayList<>();
                final List<Region> thirdRegions = secondRegion.getThirdRegions();

                for (Region thirdRegion : thirdRegions) {
                    final ReadRegionResponse thirdRegionResponse = new ReadRegionResponse(thirdRegion.getName());
                    thirdRegionResponses.add(thirdRegionResponse);
                }
                final ReadRegionResponse secondRegionResponse = new ReadRegionResponse(secondRegion.getName(),
                        thirdRegionResponses);
                secondRegionResponses.add(secondRegionResponse);
            }
            final ReadRegionResponse firstRegionResponse = new ReadRegionResponse(firstRegion.getName(), secondRegionResponses);
            readRegionResponses.add(firstRegionResponse);
        }
        return ResponseEntity.ok(readRegionResponses);
    }
}
