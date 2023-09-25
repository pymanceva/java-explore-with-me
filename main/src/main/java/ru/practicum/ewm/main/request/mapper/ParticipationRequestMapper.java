package ru.practicum.ewm.main.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.request.model.ParticipationRequest;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParticipationRequestMapper {

    public static ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto result = new ParticipationRequestDto();

        result.setId(participationRequest.getId());
        result.setEventId(participationRequest.getEvent().getId());
        result.setRequesterId(participationRequest.getRequester().getId());
        result.setCreated(participationRequest.getCreated());
        result.setState(participationRequest.getState());

        return result;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(Iterable<ParticipationRequest> requests) {
        List<ParticipationRequestDto> result = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            result.add(mapToParticipationRequestDto(request));
        }
        return result;
    }
}
