package ru.practicum.stat.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.stat.service.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("POST/saveHit");
        return statService.saveHit(endpointHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatDto> getViewStats(@RequestParam("start") LocalDateTime start,
                                          @RequestParam("end") LocalDateTime end,
                                          @RequestParam(value = "uris", required = false, defaultValue = "") List<String> uris,
                                          @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("GET/getViewStats");
        return statService.getViewStats(start, end, uris, unique);
    }
}
