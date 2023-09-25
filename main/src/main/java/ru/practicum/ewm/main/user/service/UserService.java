package ru.practicum.ewm.main.user.service;

import ru.practicum.ewm.main.user.dto.NewUserRequest;
import ru.practicum.ewm.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    List<UserDto> getAllUsersByIds(List<Long> ids, int from, int size);
}
