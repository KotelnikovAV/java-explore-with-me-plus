package ru.practicum.user.service;

import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(List<Long> ids, int from, int size);

    User createUser(User user);

    void deleteUser(long userId);
}
