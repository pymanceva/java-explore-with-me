package ru.practicum.ewm.main.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.model.Compilation;
import ru.practicum.ewm.main.event.model.Event;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static Compilation mapToCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation result = new Compilation();

        result.setEvents(events);
        result.setTitle(newCompilationDto.getTitle());
        result.setIsPinned(newCompilationDto.getPinned());

        return result;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        CompilationDto result = new CompilationDto();

        result.setId(compilation.getId());
        result.setTitle(compilation.getTitle());
        result.setEvents(compilation.getEvents());
        result.setPinned(compilation.getIsPinned());

        return result;
    }

    public static List<CompilationDto> mapToCompilationDto(Iterable<Compilation> compilations) {
        List<CompilationDto> result = new ArrayList<>();

        for (Compilation compilation : compilations) {
            result.add(mapToCompilationDto(compilation));
        }

        return result;
    }
}
