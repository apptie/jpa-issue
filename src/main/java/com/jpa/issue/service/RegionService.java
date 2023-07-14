package com.jpa.issue.service;

import com.jpa.issue.dto.OpenApiAccessTokenResponse;
import com.jpa.issue.dto.OpenApiRegionResponse;
import com.jpa.issue.dto.RegionResponse;
import com.jpa.issue.entity.Region;
import com.jpa.issue.repository.RegionRepository;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public void processRegions() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://sgisapi.kostat.go.kr")
                .path("/OpenAPI3/auth/authentication.json")
                .queryParam("consumer_key", "secret")
                .queryParam("consumer_secret", "secret")
                .encode()
                .build()
                .toUri();

        final RestTemplate restTemplate = new RestTemplate();
        final OpenApiAccessTokenResponse accessTokenResponse = restTemplate.getForObject(uri, OpenApiAccessTokenResponse.class);

        final String accessToken = accessTokenResponse.getResult().get("accessToken");

        final URI firstRegions = regionUriBuilder(accessToken, null).toUri();

        final OpenApiRegionResponse regionResponse = restTemplate.getForObject(firstRegions, OpenApiRegionResponse.class);
        final List<RegionResponse> firstRegionsResponses = regionResponse.getResult();

        for (RegionResponse firstRegionResponse : firstRegionsResponses) {
            final Region firstRegion = firstRegionResponse.toEntity();
            final String firstRegionCd = firstRegionResponse.getCd();

            final UriComponents secondRegionUri = regionUriBuilder(accessToken, firstRegionCd);

            final List<RegionResponse> secondRegionsResponses = restTemplate.getForObject(secondRegionUri.toUri(),
                    OpenApiRegionResponse.class).getResult();

            for (RegionResponse secondRegionsResponse : secondRegionsResponses) {
                final Region secondRegion = secondRegionsResponse.toEntity();
                final String secondRegionCd = secondRegionsResponse.getCd();

                final UriComponents thirdRegionUri = regionUriBuilder(accessToken, secondRegionCd);

                final List<RegionResponse> thirdRegionsResponses = restTemplate.getForObject(thirdRegionUri.toUri(),
                        OpenApiRegionResponse.class).getResult();

                firstRegion.initSecondRegion(secondRegion);

                for (RegionResponse thirdRegionsResponse : thirdRegionsResponses) {
                    final Region thirdRegion = thirdRegionsResponse.toEntity();
                    secondRegion.initThirdRegion(thirdRegion);
                }
            }
            regionRepository.save(firstRegion);
        }
    }

    private UriComponents regionUriBuilder(String accessToken, String cd) {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("https://sgisapi.kostat.go.kr")
                .path("/OpenAPI3/addr/stage.json")
                .queryParam("accessToken", accessToken);

        if (cd != null && cd.length() > 0) {
            return builder
                    .queryParam("cd", cd)
                    .encode()
                    .build();
        }
        return builder
                .encode()
                .build();
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
