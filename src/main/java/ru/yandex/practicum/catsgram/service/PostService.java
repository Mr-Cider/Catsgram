package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    UserService userService;


    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAllParam(int from, int size, String sort) {
        return posts.values().stream()
                .sorted(getSorted(sort))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Optional<Post> findById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post create(Post post) {
        // проверяем выполнение необходимых условий
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (userService.getUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("«Автор с id = " + post.getAuthorId() + " не найден»");
        }
        // формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        posts.put(post.getId(), post);
        return post;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private Comparator<Post> getSorted(String sorted) {
        if (sorted.equals("asc")) {
            return Comparator.comparing(Post::getPostDate);
        } else if (sorted.equals("desc")) {
            return Comparator.comparing(Post::getPostDate).reversed();
        } else {
            throw new IllegalArgumentException("Ошибка при сортировке");
        }
    }
}

