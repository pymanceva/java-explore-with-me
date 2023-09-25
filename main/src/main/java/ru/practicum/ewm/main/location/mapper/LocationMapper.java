package ru.practicum.ewm.main.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.location.dto.LocationDto;
import ru.practicum.ewm.main.location.dto.NewLocationDto;
import ru.practicum.ewm.main.location.model.Location;

@UtilityClass
public class LocationMapper {
    public static LocationDto mapToLocationDto(Location location) {
        LocationDto result = new LocationDto();

        result.setId(location.getId());
        result.setLat(location.getLat());
        result.setLon(location.getLon());

        return result;
    }

    public static Location mapToLocation(NewLocationDto newLocationDto) {
        Location result = new Location();

        result.setLat(newLocationDto.getLat());
        result.setLon(newLocationDto.getLon());

        return result;
    }

    public static Location mapToLocation(LocationDto locationDto) {
        Location result = new Location();

        result.setLat(locationDto.getLat());
        result.setLon(locationDto.getLon());

        return result;
    }

    public static LocationDto mapToLocationDto(NewLocationDto newLocationDto) {
        LocationDto result = new LocationDto();

        result.setLat(newLocationDto.getLat());
        result.setLon(newLocationDto.getLon());

        return result;
    }

    public static NewLocationDto mapToNewLocationDto(Location location) {
        NewLocationDto result = new NewLocationDto();

        result.setLat(location.getLat());
        result.setLon(location.getLon());

        return result;
    }
}
