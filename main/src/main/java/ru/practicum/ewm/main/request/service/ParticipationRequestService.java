package ru.practicum.ewm.main.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    @Transactional
    ParticipationRequestDto saveRequest(Long userId, Long eventId);

    @Transactional
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllRequestsByUser(Long userId);
}
