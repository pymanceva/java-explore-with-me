package ru.practicum.ewm.main.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.request.model.RequestState;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull
    private LinkedList<Long> requestIds;
    @NotNull
    private RequestState status;
}
