package ru.practicum.ewm.main.compilation.model;

import lombok.*;
import ru.practicum.ewm.main.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "compilation_pinned")
    private Boolean isPinned;

    @Column(name = "compilation_title")
    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;
}
