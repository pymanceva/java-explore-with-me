package ru.practicum.stat.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.stat.service.exception.NotSavedException;
import ru.practicum.stat.service.mapper.EndpointHitMapper;
import ru.practicum.stat.service.mapper.ViewStatMapper;
import ru.practicum.stat.service.model.EndpointHit;
import ru.practicum.stat.service.repository.EndpointHitRepository;

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
