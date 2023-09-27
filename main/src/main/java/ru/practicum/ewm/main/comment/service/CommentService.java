package ru.practicum.ewm.main.comment.service;

import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long eventId, Long userId, NewCommentDto newCommentDto);

    CommentDto updateCommentByAuthor(Long commentId, Long userId, NewCommentDto newCommentDto);

    void deleteCommentByAuthor(Long commentId, Long userId);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> getAllCommentsByEventId(Long eventId, int from, int size);

    List<CommentDto> getAllCommentsByAuthorId(Long authorId, int from, int size);

    CommentDto getCommentById(Long commentId);
}
