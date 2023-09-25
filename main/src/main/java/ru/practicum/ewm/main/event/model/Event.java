package ru.practicum.ewm.main.event.model;

import lombok.*;
import ru.practicum.ewm.main.category.model.Category;
import ru.practicum.ewm.main.location.model.Location;
import ru.practicum.ewm.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "event_category_id")
    private Category category;

    @Column(name = "event_created_on")
    private LocalDateTime createdOn;

    @Column(name = "event_description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "event_initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "event_location_id")
    private Location location;

    @Column(name = "event_paid")
    private Boolean paid;

    @Column(name = "event_participant_limit")
    private Integer participantLimit;

    @Column(name = "event_published_on")
    private LocalDateTime publishedOn;

    @Column(name = "event_request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_state")
    private EventState state;

    @Column(name = "event_name")
    private String title;
}
