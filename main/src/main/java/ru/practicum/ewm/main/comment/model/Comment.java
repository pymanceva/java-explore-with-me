package ru.practicum.ewm.main.comment.model;

import lombok.*;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "comment_author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "comment_event_id")
    private Event event;

    @Column(name = "comment_created_on")
    private LocalDateTime createdOn;
}
