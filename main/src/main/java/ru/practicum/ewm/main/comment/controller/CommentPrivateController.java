package ru.practicum.ewm.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto saveComment(@PathVariable Long eventId,
                                  @PathVariable Long userId,
                                  @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST/saveComment");
        return commentService.addComment(eventId, userId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("PATCH/updateCommentByAuthor");
        return commentService.updateCommentByAuthor(commentId, userId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        log.info("DELETE/deleteCommentByAuthor");
        commentService.deleteCommentByAuthor(commentId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CommentDto> getAllCommentsByEventId(@PathVariable Long eventId,
                                             @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                             @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("GET/getAllCommentsByEventId");
        return commentService.getAllCommentsByEventId(eventId, from, size);
    }
}
