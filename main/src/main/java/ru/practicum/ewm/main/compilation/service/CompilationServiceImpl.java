package ru.practicum.ewm.main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.main.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.main.compilation.model.Compilation;
import ru.practicum.ewm.main.compilation.repository.CompilationRepository;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.ConflictException;
import ru.practicum.ewm.main.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }

        List<Event> events = new ArrayList<>();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }

        Compilation compilation = CompilationMapper.mapToCompilation(newCompilationDto, events);

        try {
            compilationRepository.save(compilation);
            log.info("Compilation id " + compilation.getId() + " has been saved.");
            return CompilationMapper.mapToCompilationDto(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Compilation was not saved.");
        }
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation with id=" + compId + "  was not found"));

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setIsPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }

        try {
            compilationRepository.save(compilation);
            log.info("Compilation id " + compilation.getId() + " has been updated.");
            return CompilationMapper.mapToCompilationDto(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Compilation was not saved.");
        }
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        int result = compilationRepository.deleteCompilationById(compId);

        if (result == 0) {
            throw new NotFoundException("Compilation id " + compId + " is not found.");
        }

        log.info("Existed compilation id " + compId + " has been deleted.");
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        return CompilationMapper.mapToCompilationDto(compilationRepository.findAllByPinned(
                pinned, PageRequest.of(from, size)));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation with id=" + compId + "  was not found"));

        return CompilationMapper.mapToCompilationDto(compilation);
    }
}
