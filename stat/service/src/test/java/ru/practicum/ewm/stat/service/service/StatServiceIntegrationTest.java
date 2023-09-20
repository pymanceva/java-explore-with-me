package ru.practicum.ewm.stat.service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.stat.dto.EndpointHitDto;
import ru.practicum.ewm.stat.dto.ViewStatDto;
import ru.practicum.ewm.stat.service.model.EndpointHit;
import ru.practicum.ewm.stat.service.model.ViewStat;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StatServiceIntegrationTest {
    private final EndpointHit endpointHit1 = new EndpointHit(
            1L,
            "app",
            "uri",
            "1",
            LocalDateTime.of(2023, 1, 1, 0, 0)
    );

    private final EndpointHitDto endpointHitDto1 = new EndpointHitDto(
            1L,
            "app",
            "uri",
            "1",
            LocalDateTime.of(2023, 1, 1, 0, 0)
    );

    private final ViewStat viewStat = new ViewStat(
            "app",
            "uri",
            1L
    );

    @Autowired
    private StatServiceImpl statService;

    @Test
    void saveHit() {
        EndpointHitDto result = statService.saveHit(endpointHitDto1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(endpointHitDto1.getIp(), result.getIp());
        Assertions.assertEquals(endpointHitDto1.getApp(), result.getApp());
        Assertions.assertEquals(endpointHitDto1.getUri(), result.getUri());
    }

    @Test
    void getViewStats() {
        statService.saveHit(endpointHitDto1);
        List<ViewStatDto> result = statService.getViewStats(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                new ArrayList<>(),
                false);

        Assertions.assertEquals(1, result.size());
    }
}