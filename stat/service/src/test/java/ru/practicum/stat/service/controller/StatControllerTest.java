package ru.practicum.stat.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.stat.service.service.StatService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatController.class)
class StatControllerTest {
    private final EndpointHitDto endpointHitDto = new EndpointHitDto(
            1L,
            "app",
            "uri",
            "ip",
            LocalDateTime.of(2023, 1, 1, 0, 0)
    );

    private final ViewStatDto viewStatDto = new ViewStatDto(
            "app",
            "uri",
            1L
    );

    @MockBean
    private StatService statService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void saveHit() throws Exception {
        Mockito
                .when(statService.saveHit(any()))
                .thenReturn(endpointHitDto);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void getViewStats() throws Exception {
        List<ViewStatDto> viewStatDtoList = List.of(viewStatDto);

        Mockito
                .when(statService.getViewStats(any(LocalDateTime.class), any(LocalDateTime.class), any(), anyBoolean()))
                .thenReturn(viewStatDtoList);

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("start", "2020-01-01 00:00:00")
                        .param("end", "2024-01-01 00:00:00"))
                .andExpect(status().isOk());
    }
}