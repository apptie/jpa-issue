package com.jpa.issue.controller;

import com.jpa.issue.dto.ReadRegionResponse;
import com.jpa.issue.entity.Region;
import com.jpa.issue.service.RegionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/total")
    @Cacheable(cacheNames = {"regionsCache"})
    public ResponseEntity<List<ReadRegionResponse>> findAll() {
        final List<Region> allRegions = regionService.findAllRegions();
        final List<ReadRegionResponse> firstReadRegionResponses = new ArrayList<>();

        for (Region firstRegion : allRegions) {
            final List<ReadRegionResponse> secondRegionResponses = new ArrayList<>();
            final List<Region> secondRegions = firstRegion.getSecondRegions();

            for (Region secondRegion : secondRegions) {
                final List<ReadRegionResponse> thirdRegionResponses = new ArrayList<>();
                final List<Region> thirdRegions = secondRegion.getThirdRegions();

                for (Region thirdRegion : thirdRegions) {
                    final ReadRegionResponse thirdRegionResponse = new ReadRegionResponse(
                            thirdRegion.getId(),
                            thirdRegion.getName()
                    );
                    thirdRegionResponses.add(thirdRegionResponse);
                }

                final ReadRegionResponse secondRegionResponse = new ReadRegionResponse(
                        secondRegion.getId(),
                        secondRegion.getName(),
                        thirdRegionResponses
                );
                secondRegionResponses.add(secondRegionResponse);
            }

            final ReadRegionResponse firstRegionResponse = new ReadRegionResponse(
                    firstRegion.getId(),
                    firstRegion.getName(),
                    secondRegionResponses
            );

            firstReadRegionResponses.add(firstRegionResponse);
        }
        return ResponseEntity.ok(firstReadRegionResponses);
    }

    @GetMapping
    @Cacheable(cacheNames = {"regionsCache"}, key = "{'firstRegion'}")
    public ResponseEntity<List<ReadRegionResponse>> findFirstRegions() {
        final List<ReadRegionResponse> readRegionResponses = regionService.findFirstRegions().stream()
                .map(firstRegion -> new ReadRegionResponse(firstRegion.getId(), firstRegion.getName()))
                .toList();

        return ResponseEntity.ok(readRegionResponses);
    }

    @GetMapping("/{firstRegionId}")
    @Cacheable(cacheNames = {"regionsCache"}, key = "#firstRegionId")
    public ResponseEntity<List<ReadRegionResponse>> findSecondRegion(@PathVariable Long firstRegionId) {
        final List<ReadRegionResponse> readRegionResponses = regionService.findSecondRegions(firstRegionId).stream()
                .map(secondRegion -> new ReadRegionResponse(secondRegion.getId(), secondRegion.getName()))
                .toList();

        return ResponseEntity.ok(readRegionResponses);
    }

    @GetMapping("/{firstRegionId}/{secondRegionId}")
    @Cacheable(cacheNames = {"regionsCache"}, key = "#secondRegionId")
    public ResponseEntity<List<ReadRegionResponse>> findThirdRegion(
            @PathVariable Long firstRegionId,
            @PathVariable Long secondRegionId
    ) {
        final List<ReadRegionResponse> readRegionResponses = regionService.findThirdRegions(
                        firstRegionId,
                        secondRegionId
                ).stream()
                .map(thirdRegion -> new ReadRegionResponse(thirdRegion.getId(), thirdRegion.getName()))
                .toList();

        return ResponseEntity.ok(readRegionResponses);
    }
}
