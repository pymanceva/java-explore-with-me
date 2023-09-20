package ru.practicum.ewm.main.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.exception.NotSavedException;
import ru.practicum.ewm.main.location.dto.LocationDto;
import ru.practicum.ewm.main.location.dto.NewLocationDto;
import ru.practicum.ewm.main.location.mapper.LocationMapper;
import ru.practicum.ewm.main.location.model.Location;
import ru.practicum.ewm.main.location.repository.LocationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository repository;

    @Transactional
    public LocationDto saveLocation(NewLocationDto newLocationDto) {
        Location location = LocationMapper.mapToLocation(newLocationDto);
        try {
            repository.save(location);
            log.info("New location id " + location.getId() + " has been saved.");
            return LocationMapper.mapToLocationDto(location);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Location was not saved.");
        }
    }
}
