package ru.practicum.ewm.main.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer deleteCommentById(Long id);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    List<Comment> findAllByEventId(Long eventId);

    List<Comment> findAllByAuthorId(Long authorId, Pageable pageable);
}
