package ru.practicum.ewm.stat.service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.ewm.stat.dto.EndpointHitDto;
import ru.practicum.ewm.stat.dto.ViewStatDto;
import ru.practicum.ewm.stat.service.exception.NotSavedException;
import ru.practicum.ewm.stat.service.model.EndpointHit;
import ru.practicum.ewm.stat.service.model.ViewStat;
import ru.practicum.ewm.stat.service.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatServiceImplTest {
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

    @InjectMocks
    private StatServiceImpl statService;
    @Mock
    private EndpointHitRepository endpointHitRepository;

    @Test
    void saveHitValid() {
        Mockito
                .when(endpointHitRepository.save(any(EndpointHit.class)))
                .thenReturn(endpointHit1);

        EndpointHitDto result = statService.saveHit(endpointHitDto1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(endpointHitDto1.getId(), result.getId());
        Assertions.assertEquals(endpointHitDto1.getApp(), result.getApp());
        Assertions.assertEquals(endpointHitDto1.getUri(), result.getUri());
        Assertions.assertEquals(endpointHitDto1.getIp(), result.getIp());

        verify(endpointHitRepository, times(1)).save(any(EndpointHit.class));
    }

    @Test
    void saveHitInvalidAndThrow() {
        Mockito
                .when(endpointHitRepository.save(any(EndpointHit.class)))
                .thenThrow(new DataIntegrityViolationException("EndpointHit was not saved."));

        final NotSavedException ex = assertThrows(NotSavedException.class,
                () -> statService.saveHit(endpointHitDto1));

        assertThat("EndpointHit was not saved.", equalTo(ex.getMessage()));

        verify(endpointHitRepository, times(1)).save(any(EndpointHit.class));
    }

    @Test
    void getViewStatsWithEmptyUris() {
        Mockito
                .when(endpointHitRepository.getViewStats(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(viewStat));

        List<ViewStatDto> result = statService.getViewStats(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                new ArrayList<>(),
                false);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getViewStatsWithListOfUrisAndTrueUniqueParam() {
        Mockito
                .when(endpointHitRepository.getViewStatsByUniqueIp(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        anyList()))
                .thenReturn(List.of(viewStat));

        List<ViewStatDto> result = statService.getViewStats(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                List.of("uri"),
                true);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getViewStatsWithListOfUrisAndFalseUniqueParam() {
        Mockito
                .when(endpointHitRepository.getViewStatsByNotUniqueIp(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        anyList()))
                .thenReturn(List.of(viewStat));

        List<ViewStatDto> result = statService.getViewStats(
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                List.of("uri"),
                false);

        Assertions.assertEquals(1, result.size());
    }
}