package ru.practicum.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                  @RequestParam(required = false) List<EventState> states,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) LocalDateTime rangeStart,
                                                  @RequestParam(required = false) LocalDateTime rangeEnd,
                                                  @RequestParam(required = false, defaultValue = "0") int from,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET/getAllEventsByAdmin");
        return eventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH/updateEventByAdmin");
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}
