package ru.practicum.ewm.main.request.model;

import lombok.*;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_created")
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "request_event_id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "request_requester_id")
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_state")
    RequestState state;
}
