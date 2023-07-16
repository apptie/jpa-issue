package com.jpa.issue.service;

import com.jpa.issue.dto.RegionResponse;
import com.jpa.issue.entity.Region;
import java.util.List;

public interface RegionProcessor {

    String authentication();

    List<RegionResponse> requestFirstRegions(String accessToken);

    Region requestFullRegionsByFirstRegion(String accessToken, RegionResponse firstRegionResponse);
}
