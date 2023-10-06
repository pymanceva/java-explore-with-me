package ru.practicum.ewm.main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;
import ru.practicum.ewm.main.comment.mapper.CommentMapper;
import ru.practicum.ewm.main.comment.model.Comment;
import ru.practicum.ewm.main.comment.repository.CommentRepository;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.ConflictException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.NotSavedException;
import ru.practicum.ewm.main.exception.ValidationException;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto addComment(Long eventId, Long userId, NewCommentDto newCommentDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User id " + userId + " is not found."));

        Comment comment = CommentMapper.mapToComment(newCommentDto);
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreatedOn(LocalDateTime.now());

        try {
            commentRepository.save(comment);
            log.info("New comment id " + comment.getId() + " has been saved.");
            return CommentMapper.mapToCommentDto(comment);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Comment was not saved.");
        }
    }

    @Override
    @Transactional
    public CommentDto updateCommentByAuthor(Long commentId, Long userId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id=" + commentId + " was not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Comment with id=" + commentId + " was not written by user with id=" + userId);
        }

        if (comment.getCreatedOn().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ValidationException("Cannot update comment written more than 2 hours ago");
        }

        comment.setText(newCommentDto.getText());

        try {
            commentRepository.save(comment);
            log.info("New comment id " + comment.getId() + " has been updated.");
            return CommentMapper.mapToCommentDto(comment);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("Comment was not updated.");
        }
    }

    @Override
    @Transactional
    public void deleteCommentByAuthor(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id=" + commentId + " was not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("Comment with id=" + commentId + " was not written by user with id=" + userId);
        }

        int result = commentRepository.deleteCommentById(commentId);

        if (result == 0) {
            throw new NotFoundException("Comment id " + commentId + " is not found.");
        }

        log.info("Existed comment id " + commentId + " has been deleted by author.");
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id=" + commentId + " was not found"));

        int result = commentRepository.deleteCommentById(commentId);

        if (result == 0) {
            throw new NotFoundException("Comment id " + commentId + " is not found.");
        }

        log.info("Existed comment id " + commentId + " has been deleted by admin.");
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        log.info("List of comments of event with id=" + eventId + " has been gotten.");

        return CommentMapper.mapToCommentDto(commentRepository.findAllByEventId(
                eventId,
                PageRequest.of(from, size)));
    }

    @Override
    public List<CommentDto> getAllCommentsByAuthorId(Long authorId, int from, int size) {
        User author = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("User id " + authorId + " is not found."));

        log.info("List of comments of author with id=" + authorId + " has been gotten.");

        return CommentMapper.mapToCommentDto(commentRepository.findAllByAuthorId(authorId, PageRequest.of(from, size)));
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id=" + commentId + " was not found."));

        log.info("Comment with id=" + commentId + " has been gotten.");

        return CommentMapper.mapToCommentDto(comment);
    }
}
