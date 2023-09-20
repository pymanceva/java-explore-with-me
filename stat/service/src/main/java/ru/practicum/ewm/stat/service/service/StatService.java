package ru.practicum.ewm.stat.service.service;


import ru.practicum.ewm.stat.dto.EndpointHitDto;
import ru.practicum.ewm.stat.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
