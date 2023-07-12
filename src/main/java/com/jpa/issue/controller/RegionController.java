package com.jpa.issue.controller;

import com.jpa.issue.dto.CreateRegionRequest;
import com.jpa.issue.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping("/first")
    public ResponseEntity<Long> addFirstRegion(@RequestBody CreateRegionRequest request) {
        final Long firstRegionId = regionService.addFirstRegion(request.getName());

        return ResponseEntity.ok(firstRegionId);
    }

    @PostMapping("/second/{firstRegionId}")
    public ResponseEntity<Long> addSecondRegion(@RequestBody CreateRegionRequest request,
            @PathVariable Long firstRegionId) {
        final Long secondRegionId = regionService.addSecondRegion(request.getName(), firstRegionId);

        return ResponseEntity.ok(secondRegionId);
    }

    @PostMapping("/third/{secondRegionId}")
    public ResponseEntity<Long> addThirdRegion(@RequestBody CreateRegionRequest request,
            @PathVariable Long secondRegionId) {
        final Long thirdRegionId = regionService.addThirdRegion(request.getName(), secondRegionId);

        return ResponseEntity.ok(thirdRegionId);
    }
}
