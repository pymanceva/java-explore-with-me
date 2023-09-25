package ru.practicum.ewm.main.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.NotSavedException;
import ru.practicum.ewm.main.user.dto.NewUserRequest;
import ru.practicum.ewm.main.user.dto.UserDto;
import ru.practicum.ewm.main.user.dto.UserMapper;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto saveUser(NewUserRequest newUserRequest) {
        User user = UserMapper.mapToUser(newUserRequest);
        try {
            repository.save(user);
            log.info("New user id " + user.getId() + " has been saved.");
            return UserMapper.mapToUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException("User was not saved.");
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        int result = repository.deleteUserById(userId);

        if (result == 0) {
            throw new NotFoundException("User id " + userId + " is not found.");
        }

        log.info("Existed user id " + userId + " has been deleted.");
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = repository.findById(userId).orElseThrow(() ->
                new NotFoundException("User id " + userId + " is not found."));
        log.info("User id " + userId + " has been gotten.");
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("List of all users has been gotten.");
        return UserMapper.mapToUserDto(repository.findAll());
    }

    @Override
    public List<UserDto> getAllUsersByIds(List<Long> ids, int from, int size) {
        log.info("List of users by array ids has been gotten.");
        return UserMapper.mapToUserDto(repository.getAllUsersByIds(ids, PageRequest.of(from, size)));
    }
}
