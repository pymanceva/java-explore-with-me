package ru.practicum.ewm.main.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                                   @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("GET/getAllCompilations");
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("GET/getCompilationById");
        return compilationService.getCompilationById(compId);
    }
}
