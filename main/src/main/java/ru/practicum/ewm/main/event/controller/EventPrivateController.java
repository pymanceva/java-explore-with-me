package ru.practicum.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.dto.NewEventDto;
import ru.practicum.ewm.main.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.event.service.EventService;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST/addEvent");
        return eventService.saveEvent(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByInitiator(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH/updateEventByInitiator");
        return eventService.updateEventByInitiator(userId, eventId, updateEventUserRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEventsByInitiator(@PathVariable Long userId,
                                                       @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("GET/getAllEventsByInitiator");
        return eventService.getAllEventsByInitiator(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdByInitiator(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.info("GET/getEventByIdByInitiator");
        return eventService.getEventByIdByInitiator(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAllRequestsByEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET/getAllRequestsByEvent");
        return eventService.getAllRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    EventRequestStatusUpdateResult approveRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                  @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("PATCH/approveRequest");
        return eventService.approveRequest(userId, eventId, updateRequest);
    }
}
