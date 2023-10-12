package ru.practicum.ewm.main.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;
import ru.practicum.ewm.main.comment.model.Comment;
import ru.practicum.ewm.main.event.mapper.EventMapper;
import ru.practicum.ewm.main.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public static Comment mapToComment(NewCommentDto newCommentDto) {
        Comment result = new Comment();

        result.setText(newCommentDto.getText());

        return result;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto result = new CommentDto();

        result.setId(comment.getId());
        result.setText(comment.getText());
        result.setAuthor(UserMapper.mapToUserShortDto(comment.getAuthor()));
        result.setEvent(EventMapper.mapToEventShortDto(comment.getEvent(),
                CategoryMapper.mapToCategoryDto(comment.getEvent().getCategory()),
                0L,
                UserMapper.mapToUserShortDto(comment.getEvent().getInitiator()),
                0L));
        result.setCreatedOn(comment.getCreatedOn());

        return result;
    }

    public static List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(mapToCommentDto(comment));
        }

        return result;
    }
}
