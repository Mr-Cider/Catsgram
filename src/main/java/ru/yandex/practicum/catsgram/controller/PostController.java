package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("{id}")
    public Optional<Post> getPostById(@PathVariable Optional<Long> id) {
        return postService.findById(id.get());
    }

    @GetMapping
    public Collection<Post> findAllParam(
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        if (!(sort.equals("desc") || sort.equals("asc"))) {
            throw new ParameterNotValidException("sort", " Получено: " + sort + " ,должно быть: ask или desc");
        }

        if (from <= 0) {
            throw new ParameterNotValidException("from", " Начало выборки должно быть положительным числом");
        }

        if (size < 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }
        return postService.findAllParam(from, size, sort);
    }




    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}