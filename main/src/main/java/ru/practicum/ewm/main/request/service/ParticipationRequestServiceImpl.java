package ru.practicum.ewm.main.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.ConflictException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.NotSavedException;
import ru.practicum.ewm.main.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.main.request.model.ParticipationRequest;
import ru.practicum.ewm.main.request.model.RequestState;
import ru.practicum.ewm.main.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ParticipationRequestDto saveRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User id " + userId + " is not found."));

        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("Cannot apply for participation in event where you are an initiator.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot apply for participation in unpublished event.");
        }

        if (event.getParticipantLimit() > 0) {
            if (event.getParticipantLimit() <= requestRepository.getCountByEventIdAndState(eventId, RequestState.CONFIRMED)) {
                throw new ConflictException("Number of participation requests has reached the limit.");
            }
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);

        if (event.getRequestModeration()) {
            request.setState(RequestState.PENDING);
        } else {
            request.setState(RequestState.CONFIRMED);
        }

        if (event.getParticipantLimit() == 0) {
            request.setState(RequestState.CONFIRMED);
        }

        try {
            requestRepository.save(request);
            log.info("Request id=" + request.getId() + " has been saved.");
            return ParticipationRequestMapper.mapToParticipationRequestDto(request);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Request was not saved.");
        }
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User id " + userId + " is not found."));
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request with id=" + requestId + " is not found."));

        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Request with id=" + requestId + " is not found.");
        }

        request.setState(RequestState.CANCELED);

        try {
            requestRepository.save(request);
            log.info("Request id=" + request.getId() + " has been updated.");
            return ParticipationRequestMapper.mapToParticipationRequestDto(request);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Request was not saved.");
        }
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByUser(Long userId) {
        return ParticipationRequestMapper.mapToParticipationRequestDto(requestRepository.findAllByRequesterId(userId));
    }
}
