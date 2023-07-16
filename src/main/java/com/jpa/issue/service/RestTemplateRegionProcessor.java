package com.jpa.issue.service;

import com.jpa.issue.dto.OpenApiAccessTokenResponse;
import com.jpa.issue.dto.OpenApiRegionResponse;
import com.jpa.issue.dto.RegionResponse;
import com.jpa.issue.entity.Region;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RestTemplateRegionProcessor implements RegionProcessor {

    private static final String REGION_API_DOMAIN = "https://sgisapi.kostat.go.kr";
    private static final String REGION_CODE_NAME = "cd";
    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String SERVICE_KEY_NAME = "consumer_key";
    private static final String SECRET_KEY_NAME = "consumer_secret";
    private static final String AUTHENTICATION_URL = "/OpenAPI3/auth/authentication.json";
    private static final String REGION_URL_NAME = "/OpenAPI3/addr/stage.json";

    @Value("${open.api.region.service}")
    private String serviceSecret;

    @Value("${open.api.region.key}")
    private String keySecret;

    private final RestTemplate restTemplate;

    public RestTemplateRegionProcessor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Region> requestTotalRegions() {
        final String accessToken = processAuthentication();
        final List<RegionResponse> firstRegionsResponses = getRegionResponses(null, accessToken);

        final List<Region> totalRegions = new ArrayList<>();

        for (RegionResponse firstRegionResponse : firstRegionsResponses) {
            System.out.println("firstRegionResponse.getRegionName() = " + firstRegionResponse.getRegionName());
            final Region firstRegion = firstRegionResponse.toEntity();
            final List<RegionResponse> secondRegionsResponses = getRegionResponses(firstRegionResponse.getCd(), accessToken);

            for (RegionResponse secondRegionsResponse : secondRegionsResponses) {
                final Region secondRegion = secondRegionsResponse.toEntity();
                final List<RegionResponse> thirdRegionsResponses = getRegionResponses(secondRegionsResponse.getCd(), accessToken);

                firstRegion.initSecondRegion(secondRegion);
                for (RegionResponse thirdRegionsResponse : thirdRegionsResponses) {
                    final Region thirdRegion = thirdRegionsResponse.toEntity();
                    secondRegion.initThirdRegion(thirdRegion);
                }
            }

            totalRegions.add(firstRegion);
        }

        return totalRegions;
    }

    private String processAuthentication() {
        URI authenticationUri = UriComponentsBuilder
                .fromUriString(REGION_API_DOMAIN)
                .path(AUTHENTICATION_URL)
                .queryParam(SERVICE_KEY_NAME, serviceSecret)
                .queryParam(SECRET_KEY_NAME, keySecret)
                .encode()
                .build()
                .toUri();

        try {
            final OpenApiAccessTokenResponse accessTokenResponse = restTemplate.getForObject(authenticationUri, OpenApiAccessTokenResponse.class);
            return accessTokenResponse.getResult()
                    .get("accessToken");
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RegionResponse> getRegionResponses(String cd, String accessToken) {
        final UriComponents regionUri = createRegionUriBuilder(accessToken, cd);
        final OpenApiRegionResponse regionResponse = restTemplate.getForObject(regionUri.toUri(), OpenApiRegionResponse.class);

        try {
            return regionResponse.getResult();
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private UriComponents createRegionUriBuilder(String accessToken, String cd) {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(REGION_API_DOMAIN)
                .path(REGION_URL_NAME)
                .queryParam(ACCESS_TOKEN_NAME, accessToken);

        if (cd != null && !cd.isEmpty()) {
            builder.queryParam(REGION_CODE_NAME, cd);
        }

        return builder
                .encode()
                .build();
    }
}
