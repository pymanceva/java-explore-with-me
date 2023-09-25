package ru.practicum.ewm.main.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateCompilationRequest {
    private Boolean pinned;
    @Size(max = 50)
    private String title;
    private List<Long> events;
}
