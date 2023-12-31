package ru.practicum.ewm.stat.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stat.dto.EndpointHitDto;
import ru.practicum.ewm.stat.dto.ViewStatDto;
import ru.practicum.ewm.stat.service.exception.BadRequestException;
import ru.practicum.ewm.stat.service.exception.NotSavedException;
import ru.practicum.ewm.stat.service.mapper.EndpointHitMapper;
import ru.practicum.ewm.stat.service.mapper.ViewStatMapper;
import ru.practicum.ewm.stat.service.model.EndpointHit;
import ru.practicum.ewm.stat.service.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final EndpointHitRepository repository;

    @Override
    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.mapToEndpointHit(endpointHitDto);
        try {
            repository.save(endpointHit);
            log.info("New endpointHit id " + endpointHit.getId() + " has been saved.");
            return EndpointHitMapper.mapToEndpointHitDto(endpointHit);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("EndpointHit was not saved.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start != null && end != null) {
            if (end.isBefore(start)) {
                throw new BadRequestException("End before start");
            }
        }
        if (uris.isEmpty()) {
            log.info("List of ViewStats for empty URIs has been gotten.");
            return ViewStatMapper.mapToViewStatDto(repository.getViewStats(start, end));
        } else if (unique) {
            log.info("List of ViewStats for defined URIs and unique IPs has been gotten.");
            return ViewStatMapper.mapToViewStatDto(repository.getViewStatsByUniqueIp(start, end, uris));
        } else {
            log.info("List of ViewStats for defined URIs and not unique IPs has been gotten.");
            return ViewStatMapper.mapToViewStatDto(repository.getViewStatsByNotUniqueIp(start, end, uris));
        }
    }
}
