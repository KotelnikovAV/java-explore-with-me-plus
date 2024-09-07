package ru.practicum.user.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers(List<Long> ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (CollectionUtils.isEmpty(ids)) {
            return userRepository.findAll(pageRequest).getContent();
        } else {
            return userRepository.findAllByIdIn(ids, pageRequest).getContent();
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        userRepository.findUserByEmail(user.getEmail()).ifPresent(u -> {
            throw new IntegrityViolationException("User with email " + u.getEmail() + " already exists");
        });
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id = " + userId + " not found"));
        userRepository.deleteById(userId);
    }
}
