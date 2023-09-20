package ru.practicum.ewm.main.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.event.controller.EventPublicController;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    @Transactional
    EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventShortDto> getAllEventsByInitiator(Long userId, int from, int size);

    EventFullDto getEventByIdByInitiator(Long userId, Long eventId);

    List<EventFullDto> getAllEventsByAdmin(List<Long> users,
                                           List<EventState> states,
                                           List<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           int from,
                                           int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getAllPublishedEvents(String text,
                                              List<Long> categories,
                                              Boolean paid,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              boolean onlyAvailable,
                                              EventPublicController.Sort sort,
                                              int from,
                                              int size,
                                              HttpServletRequest request);

    EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request);

    List<ParticipationRequestDto> getAllRequestsByEvent(Long userId, Long eventId);

    @Transactional
    EventRequestStatusUpdateResult approveRequest(Long userId, Long eventId,
                                                  EventRequestStatusUpdateRequest updateRequest);
}
