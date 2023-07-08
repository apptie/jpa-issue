package com.jpa.issue.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.jpa.issue.repository.StationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class StationTest {

    @MockBean
    Clock clock;

    @PersistenceContext
    EntityManager em;

    @Autowired
    StationRepository stationRepository;

    @Test
    void clockTest() {
        final Instant instant = Instant.parse("2023-07-08T22:21:20Z");
        final ZoneId zoneId = ZoneId.of("UTC");

        given(clock.instant()).willReturn(instant);
        given(clock.getZone()).willReturn(zoneId);

        final Station station = new Station("잠실역");

        stationRepository.save(station);

        em.flush();
        em.clear();

        final LocalDateTime expected = instant.atZone(zoneId).toLocalDateTime();
        final Station actual = stationRepository.findById(station.getId()).get();

        assertThat(actual.getId()).isEqualTo(station.getId());
        assertThat(actual.getCreatedDate()).isEqualTo(expected);
        assertThat(actual.getLastModifiedDate()).isEqualTo(expected);
    }
}
