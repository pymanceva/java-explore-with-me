package ru.practicum.stat.service.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ViewStat {
    private String app;
    @Id
    private String uri;
    private Long hits;
}
