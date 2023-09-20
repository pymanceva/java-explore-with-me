package ru.practicum.ewm.stat.service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.stat.dto.ViewStatDto;
import ru.practicum.ewm.stat.service.model.EndpointHit;
import ru.practicum.ewm.stat.service.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EndpointHitRepositoryTest {
    private final EndpointHit endpointHit1 = new EndpointHit(
            1L,
            "app",
            "uri",
            "1",
            LocalDateTime.of(2023, 1, 1, 0, 0)
    );

    private final EndpointHit endpointHit2 = new EndpointHit(
            2L,
            "app",
            "uri",
            "2",
            LocalDateTime.of(2023, 1, 1, 0, 1)
    );

    private final EndpointHit endpointHit3 = new EndpointHit(
            3L,
            "app",
            "uri",
            "1",
            LocalDateTime.of(2023, 1, 1, 0, 2)
    );

    private final ViewStatDto viewStatDto2 = new ViewStatDto(
            "app",
            "uri",
            2L
    );

    @Autowired
    private TestEntityManager em;
    @Autowired
    private EndpointHitRepository endpointHitRepository;

    @BeforeEach
    void setup() {
        endpointHitRepository.save(endpointHit1);
        endpointHitRepository.save(endpointHit2);
        endpointHitRepository.save(endpointHit3);
    }

    @AfterEach
    void deleteAll() {
        endpointHitRepository.deleteAll();
    }

    @Test
    void getViewStats() {
        List<ViewStat> result = endpointHitRepository.getViewStats(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0)
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(3L, result.get(0).getHits());
        Assertions.assertEquals(viewStatDto2.getApp(), result.get(0).getApp());
        Assertions.assertEquals(viewStatDto2.getUri(), result.get(0).getUri());
    }

    @Test
    void getViewStatsByUniqueIp() {
        List<ViewStat> result = endpointHitRepository.getViewStatsByUniqueIp(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                List.of("uri")
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2L, result.get(0).getHits());
        Assertions.assertEquals(viewStatDto2.getApp(), result.get(0).getApp());
        Assertions.assertEquals(viewStatDto2.getUri(), result.get(0).getUri());
    }

    @Test
    void getViewStatsByNotUniqueIp() {
        List<ViewStat> result = endpointHitRepository.getViewStatsByUniqueIp(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                List.of("uri")
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2L, result.get(0).getHits());
        Assertions.assertEquals(viewStatDto2.getApp(), result.get(0).getApp());
        Assertions.assertEquals(viewStatDto2.getUri(), result.get(0).getUri());
    }
}