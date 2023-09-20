package ru.practicum.ewm.main.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto result = new UserDto();

        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());

        return result;
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(mapToUserDto(user));
        }

        return result;
    }

    public static UserShortDto mapToUserShortDto(User user) {
        UserShortDto result = new UserShortDto();

        result.setId(user.getId());
        result.setName(user.getName());

        return result;
    }

    public static List<UserShortDto> mapToUserShortDto(Iterable<User> users) {
        List<UserShortDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(mapToUserShortDto(user));
        }

        return result;
    }

    public static NewUserRequest mapToNewUserRequest(User user) {
        NewUserRequest result = new NewUserRequest();

        result.setName(user.getName());
        result.setEmail(user.getEmail());

        return result;
    }

    public static User mapToUser(NewUserRequest newUserRequest) {
        User result = new User();

        result.setName(newUserRequest.getName());
        result.setEmail(newUserRequest.getEmail());

        return result;
    }

    public static User mapToUser(UserShortDto userShortDto) {
        User result = new User();

        result.setId(userShortDto.getId());
        result.setName(userShortDto.getName());

        return result;
    }

    public static User mapToUser(UserDto userDto) {
        User result = new User();

        result.setId(userDto.getId());
        result.setName(userDto.getName());

        return result;
    }
}
