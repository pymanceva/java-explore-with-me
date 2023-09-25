package ru.practicum.ewm.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotNull
    private Long hits;
}
