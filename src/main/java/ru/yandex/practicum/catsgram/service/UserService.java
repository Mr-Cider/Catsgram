package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {


    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User createUser( User user) {
        if ((user.getEmail() == null) || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new ConditionsNotMetException("Этот имейл уже используется");
            }
        }
        user.setId(userGetNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(user.getId())) {
            if (user.getEmail() == null) {
                user.setEmail(users.get(user.getId()).getEmail());
            }
            if (user.getUsername() == null) {
                user.setUsername(users.get(user.getId()).getUsername());
            }
            if (user.getPassword() == null) {
                user.setPassword(users.get(user.getId()).getPassword());
            }
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
    }

    private long userGetNextId() {
        return users.keySet().stream()
                .mapToLong(id -> id)
                .max().orElse(0) + 1;
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
