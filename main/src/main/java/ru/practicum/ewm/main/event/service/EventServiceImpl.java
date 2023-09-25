package ru.practicum.ewm.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.category.model.Category;
import ru.practicum.ewm.main.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.event.controller.EventPublicController;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.event.mapper.EventMapper;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.event.model.StateActionByAdmin;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.BadRequestException;
import ru.practicum.ewm.main.exception.ConflictException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.NotSavedException;
import ru.practicum.ewm.main.location.dto.NewLocationDto;
import ru.practicum.ewm.main.location.mapper.LocationMapper;
import ru.practicum.ewm.main.location.model.Location;
import ru.practicum.ewm.main.location.repository.LocationRepository;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.main.request.model.ParticipationRequest;
import ru.practicum.ewm.main.request.model.RequestState;
import ru.practicum.ewm.main.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.main.user.dto.UserMapper;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;
import ru.practicum.ewm.stat.client.StatClient;
import ru.practicum.ewm.stat.dto.EndpointHitDto;
import ru.practicum.ewm.stat.dto.ViewStatDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatClient statClient;

    @Transactional
    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        validateEventTime(newEventDto.getEventDate());

        Category category = categoryRepository.findById(newEventDto.getCategoryId()).orElseThrow(() ->
                new NotFoundException("Category with id=" + newEventDto.getCategoryId() + " was not found"));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User id " + userId + " is not found."));

        Location location = getLocationForEvent(newEventDto.getLocation());

        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }

        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }

        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }

        Event event = EventMapper.mapToEvent(newEventDto, category, user, location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        try {
            eventRepository.save(event);
            log.info("New event id " + event.getId() + " has been saved.");
            return EventMapper.mapToEventFullDto(event,
                    CategoryMapper.mapToCategoryDto(category),
                    0L,
                    UserMapper.mapToUserShortDto(user),
                    LocationMapper.mapToNewLocationDto(location),
                    0L);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Event was not saved.");
        }
    }

    @Transactional
    @Override
    public EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        if (updateEventUserRequest.getEventDate() != null) {
            validateEventTime(updateEventUserRequest.getEventDate());
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        if (updateEventUserRequest.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategoryId()).orElseThrow(() ->
                    new NotFoundException("Category with id=" + updateEventUserRequest.getCategoryId() + " was not found")));
        }

        if (updateEventUserRequest.getNewLocationDto() != null) {
            event.setLocation(getLocationForEvent(updateEventUserRequest.getNewLocationDto()));
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        try {
            eventRepository.save(event);
            log.info("Event id=" + event.getId() + " has been updated.");
            return EventMapper.mapToEventFullDto(event,
                    CategoryMapper.mapToCategoryDto(event.getCategory()),
                    0L,
                    UserMapper.mapToUserShortDto(event.getInitiator()),
                    LocationMapper.mapToNewLocationDto(event.getLocation()),
                    0L);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Event was not saved.");
        }
    }

    @Override
    public List<EventShortDto> getAllEventsByInitiator(Long userId, int from, int size) {
        return EventMapper.mapToEventShortDto(eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size)));
    }

    @Override
    public EventFullDto getEventByIdByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        return EventMapper.mapToEventFullDto(event,
                CategoryMapper.mapToCategoryDto(event.getCategory()),
                0L,
                UserMapper.mapToUserShortDto(event.getInitiator()),
                LocationMapper.mapToNewLocationDto(event.getLocation()),
                0L);
    }

    @Override
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users,
                                                  List<EventState> states,
                                                  List<Long> categories,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  int from,
                                                  int size) {
        if (users != null && users.size() == 1 && users.get(0).equals(0L)) {
            users = null;
        }

        List<Event> events = eventRepository.findAllEventsByAdmin(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                PageRequest.of(from, size));


        List<EventFullDto> eventDtos = EventMapper.mapToEventFullDto(events);

        List<EventFullDto> result = eventDtos.stream().peek(eventDto -> eventDto.setConfirmedRequests(
                        requestRepository.getCountByEventIdAndState(eventDto.getId(), RequestState.CONFIRMED)))
                .collect(Collectors.toList());

        log.info("List of events for admin by parameters has been gotten.");

        return result;
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                        "Value: " + updateEventAdminRequest.getEventDate());
            }
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionByAdmin.PUBLISH_EVENT) &&
                    !event.getState().equals(EventState.PENDING)) {
                throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
            }
            if (updateEventAdminRequest.getStateAction().equals(StateActionByAdmin.REJECT_EVENT) &&
                    event.getState().equals(EventState.PUBLISHED)) {
                throw new ConflictException("Cannot reject the event because it's not in the right state: " + event.getState());
            }
        }

        if (updateEventAdminRequest.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategoryId()).orElseThrow(() ->
                    new NotFoundException("Category with id=" + updateEventAdminRequest.getCategoryId() + " was not found")));
        }

        if (updateEventAdminRequest.getNewLocationDto() != null) {
            event.setLocation(getLocationForEvent(updateEventAdminRequest.getNewLocationDto()));
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        try {
            eventRepository.save(event);
            log.info("Event id=" + event.getId() + " has been updated.");
            return EventMapper.mapToEventFullDto(event,
                    CategoryMapper.mapToCategoryDto(event.getCategory()),
                    requestRepository.getCountByEventIdAndState(eventId, RequestState.CONFIRMED),
                    UserMapper.mapToUserShortDto(event.getInitiator()),
                    LocationMapper.mapToNewLocationDto(event.getLocation()),
                    0L);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Event was not saved.");
        }
    }

    @Override
    public List<EventShortDto> getAllPublishedEvents(String text,
                                                     List<Long> categories,
                                                     Boolean paid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     boolean onlyAvailable,
                                                     EventPublicController.Sort sort,
                                                     int from,
                                                     int size,
                                                     HttpServletRequest request) {
        if (rangeStart != null || rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException("RangeEnd cannot be before RangeStart");
            }
        }
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        saveHit(ip, url);

        List<Event> events = eventRepository.findAllEventsByParams(
                text, categories, paid, rangeStart, rangeEnd, PageRequest.of(from, size));

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit().equals(0) || event.getParticipantLimit() <
                            requestRepository.getCountByEventIdAndState(event.getId(), RequestState.CONFIRMED))
                    .collect(Collectors.toList());
        }

        List<ViewStatDto> viewStatsDtos = getEventsViewsList(events, rangeStart, rangeEnd);
        Map<String, Long> eventViews = getEventViewsMap(viewStatsDtos);
        System.out.println(viewStatsDtos);
        System.out.println(eventViews);
        List<EventShortDto> eventShortDtos = EventMapper.mapToEventShortDto(events, eventViews);
        List<EventShortDto> result = eventShortDtos.stream().peek(eventDto -> eventDto.setConfirmedRequests(
                        requestRepository.getCountByEventIdAndState(eventDto.getId(), RequestState.CONFIRMED)))
                .collect(Collectors.toList());
        System.out.println(result);
        switch (sort) {
            case EVENT_DATE:
                result.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                result.sort(Comparator.comparing(EventShortDto::getViews).reversed());
                break;
        }

        log.info("List of public events has been gotten.");

        return result;
    }

    @Override
    public EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        saveHit(ip, url);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        Map<String, Long> eventViews = getEventViewsMap(getEventsViewsList(List.of(event), LocalDateTime.MIN, LocalDateTime.MAX));

        log.info("Public event with id=" + eventId + " has been gotten.");

        return EventMapper.mapToEventFullDto(List.of(event), eventViews).get(0);
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        return ParticipationRequestMapper.mapToParticipationRequestDto(requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult approveRequest(Long userId, Long eventId,
                                                         EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        LinkedList<Long> ids = updateRequest.getRequestIds();

        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(ids);

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        long leftLimit = event.getParticipantLimit() -
                requestRepository.getCountByEventIdAndState(eventId, RequestState.CONFIRMED);

        if (leftLimit <= 0) {
            throw new ConflictException("The participant limit has been reached");
        }

        if (event.getParticipantLimit().equals(0) || !event.getRequestModeration()) {
            return result;
        }

        for (ParticipationRequest request : requests) {
            if (!request.getState().equals(RequestState.PENDING)) {
                throw new BadRequestException("Request must have status PENDING");
            }

            if (leftLimit <= 0) {
                request.setState(RequestState.REJECTED);
                rejectedRequests.add(request);
                continue;
            }

            switch (updateRequest.getStatus()) {
                case CONFIRMED:
                    request.setState(RequestState.CONFIRMED);
                    confirmedRequests.add(request);
                    leftLimit--;
                    break;
                case REJECTED:
                    request.setState(RequestState.REJECTED);
                    rejectedRequests.add(request);
                    break;
            }
        }
        result.setConfirmedRequests(ParticipationRequestMapper.mapToParticipationRequestDto(confirmedRequests));
        result.setRejectedRequests(ParticipationRequestMapper.mapToParticipationRequestDto(rejectedRequests));
        return result;
    }

    private void validateEventTime(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDate);
        }
    }

    private Location getLocationForEvent(NewLocationDto locationDto) {
        Location location = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());

        if (location == null) {
            location = locationRepository.save(LocationMapper.mapToLocation(locationDto));
        }

        return location;
    }

    private void saveHit(String ip, String url) {
        String app = "ewm-main-service";
        EndpointHitDto endpointHitDto = new EndpointHitDto(null, app, url, ip, LocalDateTime.now());
        statClient.saveHit(endpointHitDto);
    }

    private List<ViewStatDto> getEventsViewsList(List<Event> events, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<String> eventUris = events
                .stream()
                .map(e -> String.format("/events/%s", e.getId()))
                .collect(Collectors.toList());
        String start = LocalDateTime.now().minusYears(2).format(DATE_FORMATTER);
        String end = LocalDateTime.now().plusYears(2).format(DATE_FORMATTER);

        return statClient
                .getViewStats(start, end, eventUris, true);
    }

    private Map<String, Long> getEventViewsMap(List<ViewStatDto> viewStatDtos) {
        Map<String, Long> eventViews = new HashMap<>();

        for (ViewStatDto viewStat : viewStatDtos) {
            eventViews.put(viewStat.getUri(), viewStat.getHits());
        }

        return eventViews;
    }
}
