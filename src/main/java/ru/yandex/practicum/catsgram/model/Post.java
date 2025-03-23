package ru.yandex.practicum.catsgram.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Post {
    Long id;
    @NotBlank
    long authorId;
    String description;
    Instant postDate;
}
