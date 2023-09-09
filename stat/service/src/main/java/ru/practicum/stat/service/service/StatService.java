package ru.practicum.stat.service.service;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
