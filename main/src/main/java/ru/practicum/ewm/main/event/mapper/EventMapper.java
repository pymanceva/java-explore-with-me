package ru.practicum.ewm.main.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.category.model.Category;
import ru.practicum.ewm.main.category.model.dto.CategoryDto;
import ru.practicum.ewm.main.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.dto.NewEventDto;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.location.dto.NewLocationDto;
import ru.practicum.ewm.main.location.mapper.LocationMapper;
import ru.practicum.ewm.main.location.model.Location;
import ru.practicum.ewm.main.user.dto.UserMapper;
import ru.practicum.ewm.main.user.dto.UserShortDto;
import ru.practicum.ewm.main.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class EventMapper {
    public static Event mapToEvent(NewEventDto newEventDto, Category category, User user, Location location) {
        Event result = new Event();

        result.setAnnotation(newEventDto.getAnnotation());
        result.setCategory(category);
        result.setDescription(newEventDto.getDescription());
        result.setEventDate(newEventDto.getEventDate());
        result.setInitiator(user);
        result.setLocation(location);
        result.setPaid(newEventDto.getPaid());
        result.setParticipantLimit(newEventDto.getParticipantLimit());
        result.setRequestModeration(newEventDto.getRequestModeration());
        result.setTitle(newEventDto.getTitle());

        return result;
    }

    public EventFullDto mapToEventFullDto(Event event,
                                          CategoryDto categoryDto,
                                          Long confirmedRequests,
                                          UserShortDto userShortDto,
                                          NewLocationDto newLocationDto,
                                          Long views,
                                          List<CommentDto> comments) {

        EventFullDto result = new EventFullDto();

        result.setId(event.getId());
        result.setAnnotation(event.getAnnotation());
        result.setCategory(categoryDto);
        result.setConfirmedRequests(confirmedRequests);
        result.setCreatedOn(event.getCreatedOn());
        result.setDescription(event.getDescription());
        result.setEventDate(event.getEventDate());
        result.setInitiator(userShortDto);
        result.setLocation(newLocationDto);
        result.setPaid(event.getPaid());
        result.setParticipantLimit(event.getParticipantLimit());
        result.setPublishedOn(event.getPublishedOn());
        result.setRequestModeration(event.getRequestModeration());
        result.setState(event.getState());
        result.setTitle(event.getTitle());
        result.setViews(views);
        result.setComments(comments);

        return result;
    }

    public static EventShortDto mapToEventShortDto(Event event,
                                                   CategoryDto categoryDto,
                                                   Long confirmedRequests,
                                                   UserShortDto userShortDto,
                                                   Long views) {

        EventShortDto result = new EventShortDto();

        result.setId(event.getId());
        result.setAnnotation(event.getAnnotation());
        result.setCategory(categoryDto);
        result.setConfirmedRequests(confirmedRequests);
        result.setEventDate(event.getEventDate());
        result.setInitiator(userShortDto);
        result.setPaid(event.getPaid());
        result.setTitle(event.getTitle());
        result.setViews(views);

        return result;
    }

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(mapToEventShortDto(event,
                    CategoryMapper.mapToCategoryDto(event.getCategory()),
                    0L,
                    UserMapper.mapToUserShortDto(event.getInitiator()),
                    0L));
        }
        return result;
    }

    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(mapToEventFullDto(event,
                    CategoryMapper.mapToCategoryDto(event.getCategory()),
                    0L,
                    UserMapper.mapToUserShortDto(event.getInitiator()),
                    LocationMapper.mapToNewLocationDto(event.getLocation()),
                    0L, null));
        }

        return result;
    }

    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events,
                                                       Map<String, Long> views,
                                                       List<CommentDto> comments) {
        List<EventFullDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(mapToEventFullDto(event,
                    CategoryMapper.mapToCategoryDto(event.getCategory()),
                    0L,
                    UserMapper.mapToUserShortDto(event.getInitiator()),
                    LocationMapper.mapToNewLocationDto(event.getLocation()),
                    views.get(String.format("/events/" + event.getId())), comments));
        }

        return result;
    }

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events, Map<String, Long> views) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            Long hits;
            if (views.get(String.format("/events/" + event.getId())) == null) {
                hits = 0L;
            } else {
                hits = views.get(String.format("/events/" + event.getId()));
            }
            result.add(mapToEventShortDto(event,
                    CategoryMapper.mapToCategoryDto(event.getCategory()),
                    0L,
                    UserMapper.mapToUserShortDto(event.getInitiator()),
                    hits));
        }

        return result;
    }
}
