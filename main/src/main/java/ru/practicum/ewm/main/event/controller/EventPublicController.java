package ru.practicum.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllPublishedEvents(@RequestParam(required = false, defaultValue = "") String text,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) LocalDateTime rangeStart,
                                                     @RequestParam(required = false) LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                     @RequestParam(defaultValue = "VIEWS") Sort sort,
                                                     @RequestParam(required = false, defaultValue = "0") int from,
                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                     HttpServletRequest request) {
        log.info("GET/getAllPublishedEvents");
        return eventService.getAllPublishedEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getPublishedEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("GET/getPublishedEventById");
        return eventService.getPublishedEventById(id, request);
    }

    public enum Sort {
        EVENT_DATE,
        VIEWS
    }
}
