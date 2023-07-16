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
    void authenticationTest() throws Exception {
        // given
        final String expectedAccessToken = "accessToken";
        final String testServiceSecret = "testServiceSecret";
        final String testKeySecret = "testKeySecret";

        ReflectionTestUtils.setField(restTemplateRegionProcessor, "serviceSecret", testServiceSecret);
        ReflectionTestUtils.setField(restTemplateRegionProcessor, "keySecret", testKeySecret);

        final String expectedUri = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json?" +
                "consumer_key=" +
                testServiceSecret +
                "&consumer_secret=" +
                testKeySecret;

        final OpenApiAccessTokenResponse accessTokenResponse = new OpenApiAccessTokenResponse(
                Collections.singletonMap("accessToken", expectedAccessToken));

        mockRestServiceServer
                .expect(requestTo(expectedUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(accessTokenResponse), MediaType.APPLICATION_JSON));

        // when
        final String actual = restTemplateRegionProcessor.authentication();

        // then
        assertThat(actual).isEqualTo(expectedAccessToken);
    }

    @Test
    void requestFirstRegionsTest() throws Exception {
        // given
        final String accessToken = "accessToken";

        final String expectedUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken;

        final RegionResponse expectedRegionResponse = new RegionResponse("서울특별시", "11");
        final OpenApiRegionResponse openApiRegionResponse = new OpenApiRegionResponse(List.of(expectedRegionResponse));

        mockRestServiceServer
                .expect(requestTo(expectedUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(openApiRegionResponse), MediaType.APPLICATION_JSON));

        // when
        final List<RegionResponse> actualRegionResponse = restTemplateRegionProcessor.requestFirstRegions(accessToken);

        // then
        assertThat(actualRegionResponse).hasSize(1);
        final RegionResponse actualFirstRegionResponse = actualRegionResponse.get(0);
        assertThat(actualFirstRegionResponse.getRegionName()).isEqualTo(expectedRegionResponse.getRegionName());
        assertThat(actualFirstRegionResponse.getCd()).isEqualTo(expectedRegionResponse.getCd());
    }

    @Test
    void requestFullRegionsByFirstRegion() throws Exception {
        // given
        final String accessToken = "accessToken";
        final String secondRegionCd = "11";
        final String thirdRegionCd = "11230";

        final String expectedSecondRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken +
                "&cd=" +
                secondRegionCd;
        final String expectedThirdRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken +
                "&cd=" +
                thirdRegionCd;

        final RegionResponse firstRegionResponse = new RegionResponse("서울특별시", "11");

        final RegionResponse secondRegionResponse = new RegionResponse("강남구", "11230");
        final OpenApiRegionResponse secondOpenApiRegionResponse = new OpenApiRegionResponse(List.of(secondRegionResponse));

        final RegionResponse thirdRegionResponse = new RegionResponse("개포1동", "11230680");
        final OpenApiRegionResponse thirdOpenApiRegionResponse = new OpenApiRegionResponse(List.of(thirdRegionResponse));

        mockRestServiceServer
                .expect(requestTo(expectedSecondRegionUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(secondOpenApiRegionResponse), MediaType.APPLICATION_JSON));

        mockRestServiceServer
                .expect(requestTo(expectedThirdRegionUri))
                .andRespond(withSuccess(objectMapper.writeValueAsString(thirdOpenApiRegionResponse), MediaType.APPLICATION_JSON));

        // when
        final Region actualFirstRegion = restTemplateRegionProcessor.requestFullRegionsByFirstRegion(accessToken, firstRegionResponse);

        // then
        assertThat(actualFirstRegion.getName()).isEqualTo(firstRegionResponse.getRegionName());
        assertThat(actualFirstRegion.getSecondRegions()).hasSize(1);
        final Region actualSecondRegion = actualFirstRegion.getSecondRegions().get(0);
        assertThat(actualSecondRegion.getName()).isEqualTo("강남구");
        assertThat(actualSecondRegion.getThirdRegions()).hasSize(1);
        final Region actualThirdRegion = actualSecondRegion.getThirdRegions().get(0);
        assertThat(actualThirdRegion.getName()).isEqualTo("개포1동");
    }
}
