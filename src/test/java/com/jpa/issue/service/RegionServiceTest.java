package com.jpa.issue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.jpa.issue.entity.Region;
import com.jpa.issue.repository.RegionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RegionServiceTest {

    @MockBean
    RegionProcessor regionProcessor;

    @Autowired
    RegionService regionService;

    @Autowired
    RegionRepository regionRepository;

    @Test
    void test() {
        final Region firstRegion = new Region("서울특별시");
        final Region secondRegion = new Region("송파구");
        final Region thirdRegion = new Region("가락1동");
        secondRegion.initThirdRegion(thirdRegion);
        firstRegion.initSecondRegion(secondRegion);
        given(regionProcessor.requestTotalRegions()).willReturn(List.of(firstRegion));

        regionService.initializationRegions();

        final List<Region> actual = regionRepository.findAll();

        assertThat(actual).hasSize(3);
        assertThat(actual.get(0).getSecondRegions()).hasSize(1);
        final Region findSecondRegion = actual.get(0).getSecondRegions().get(0);
        assertThat(findSecondRegion).isEqualTo(secondRegion);
        assertThat(findSecondRegion.getThirdRegions()).hasSize(1);
        final Region findThirdRegion = findSecondRegion.getThirdRegions().get(0);
        assertThat(findThirdRegion).isEqualTo(thirdRegion);
    }
}
