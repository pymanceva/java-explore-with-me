package ru.practicum.ewm.main.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewLocationDto {
    @NotNull
    private float lat;
    @NotNull
    private float lon;
}
