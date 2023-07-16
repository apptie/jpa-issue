package com.jpa.issue.configuration;

import com.jpa.issue.service.RegionService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "data.init.region.enabled", havingValue = "true")
public class InitializationRegionProcessorConfiguration implements ApplicationRunner {

    private final RegionService regionService;

    public InitializationRegionProcessorConfiguration(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        regionService.initializationRegions();
    }
}
