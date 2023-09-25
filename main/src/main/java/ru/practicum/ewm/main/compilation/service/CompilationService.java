package ru.practicum.ewm.main.compilation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    @Transactional
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    @Transactional
    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    @Transactional
    void deleteCompilation(Long compId);

    List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);
}
