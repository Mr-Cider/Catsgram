package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
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

    @PutMapping
    public User updateUser(@RequestBody User user) {
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
                .max().orElse(1);
    }
}
