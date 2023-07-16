package com.jpa.issue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpa.issue.configuration.RestTemplateConfiguration;
import com.jpa.issue.dto.OpenApiAccessTokenResponse;
import com.jpa.issue.dto.OpenApiRegionResponse;
import com.jpa.issue.dto.RegionResponse;
import com.jpa.issue.entity.Region;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RestClientTest({RestTemplateRegionProcessor.class})
@Import(RestTemplateConfiguration.class)
class RestTemplateRegionProcessorTest {

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateRegionProcessor restTemplateRegionProcessor;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        objectMapper = new ObjectMapper();
    }

    @Test
    void requestTotalRegions() throws Exception {
        // given
        final String accessToken = "accessToken";
        final String testServiceSecret = "testServiceSecret";
        final String testKeySecret = "testKeySecret";

        ReflectionTestUtils.setField(restTemplateRegionProcessor, "serviceSecret", testServiceSecret);
        ReflectionTestUtils.setField(restTemplateRegionProcessor, "keySecret", testKeySecret);

        final String accessTokenUri = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json?" +
                "consumer_key=" +
                testServiceSecret +
                "&consumer_secret=" +
                testKeySecret;

        final OpenApiAccessTokenResponse accessTokenResponse = new OpenApiAccessTokenResponse(
                Collections.singletonMap("accessToken", accessToken));

        mockRestServiceServer
                .expect(requestTo(accessTokenUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(accessTokenResponse), MediaType.APPLICATION_JSON));

        final String firstRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken;

        final RegionResponse firstRegionResponse = new RegionResponse("서울특별시", "11");
        final OpenApiRegionResponse openApiRegionResponse = new OpenApiRegionResponse(List.of(firstRegionResponse));

        mockRestServiceServer
                .expect(requestTo(firstRegionUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(openApiRegionResponse), MediaType.APPLICATION_JSON));

        final String secondRegionCd = "11";

        final String secondRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken +
                "&cd=" +
                secondRegionCd;

        final RegionResponse secondRegionResponse = new RegionResponse("강남구", "11230");
        final OpenApiRegionResponse secondOpenApiRegionResponse = new OpenApiRegionResponse(List.of(secondRegionResponse));

        mockRestServiceServer
                .expect(requestTo(secondRegionUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(secondOpenApiRegionResponse), MediaType.APPLICATION_JSON));

        final String thirdRegionCd = "11230";

        final String thirdRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken +
                "&cd=" +
                thirdRegionCd;

        final RegionResponse thirdRegionResponse = new RegionResponse("개포1동", "11230680");
        final OpenApiRegionResponse thirdOpenApiRegionResponse = new OpenApiRegionResponse(List.of(thirdRegionResponse));

        mockRestServiceServer
                .expect(requestTo(thirdRegionUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(thirdOpenApiRegionResponse), MediaType.APPLICATION_JSON));

        // when
        final List<Region> firstRegions = restTemplateRegionProcessor.requestTotalRegions();

        // then
        assertThat(firstRegions).hasSize(1);
        final Region firstRegion = firstRegions.get(0);
        assertThat(firstRegion.getName()).isEqualTo(firstRegionResponse.getRegionName());
        assertThat(firstRegion.getSecondRegions()).hasSize(1);
        final Region secondRegion = firstRegion.getSecondRegions().get(0);
        assertThat(secondRegion.getName()).isEqualTo(secondRegionResponse.getRegionName());
        assertThat(secondRegion.getThirdRegions()).hasSize(1);
        final Region thirdRegion = secondRegion.getThirdRegions().get(0);
        assertThat(thirdRegion.getName()).isEqualTo(thirdRegionResponse.getRegionName());
    }
}
