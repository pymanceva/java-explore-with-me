package ru.practicum.stat.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.stat.service.model.ViewStat;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ViewStatMapper {
    public static ViewStatDto mapToViewStatDto(ViewStat viewStat) {
        ViewStatDto result = new ViewStatDto();

        result.setApp(viewStat.getApp());
        result.setUri(viewStat.getUri());
        result.setHits(viewStat.getHits());

        return result;
    }

    public static List<ViewStatDto> mapToViewStatDto(Iterable<ViewStat> viewStats) {
        List<ViewStatDto> result = new ArrayList<>();

        for (ViewStat viewStat : viewStats) {
            result.add(mapToViewStatDto(viewStat));
        }

        return result;
    }
}
