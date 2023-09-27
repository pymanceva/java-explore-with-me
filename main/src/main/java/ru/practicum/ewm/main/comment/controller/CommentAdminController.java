package ru.practicum.ewm.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("DELETE/deleteCommentByAdmin");
        commentService.deleteCommentByAdmin(commentId);
    }

    @GetMapping("/users/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllCommentsByAuthorId(@PathVariable Long authorId,
                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("GET/getAllCommentsByEventId");
        return commentService.getAllCommentsByAuthorId(authorId, from, size);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("GET/getCommentById");
        return commentService.getCommentById(commentId);
    }
}
