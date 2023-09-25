package ru.practicum.ewm.stat.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stat.dto.EndpointHitDto;
import ru.practicum.ewm.stat.service.model.EndpointHit;


@UtilityClass
public class EndpointHitMapper {

    public static EndpointHit mapToEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit result = new EndpointHit();

        result.setId(endpointHitDto.getId());
        result.setApp(endpointHitDto.getApp());
        result.setUri(endpointHitDto.getUri());
        result.setIp(endpointHitDto.getIp());
        result.setTimestamp(endpointHitDto.getTimestamp());

        return result;
    }

    public static EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit) {
        EndpointHitDto result = new EndpointHitDto();

        result.setId(endpointHit.getId());
        result.setApp(endpointHit.getApp());
        result.setUri(endpointHit.getUri());
        result.setIp(endpointHit.getIp());
        result.setTimestamp(endpointHit.getTimestamp());

        return result;
    }
}
