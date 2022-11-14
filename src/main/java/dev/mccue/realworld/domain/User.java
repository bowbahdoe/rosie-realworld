package dev.mccue.realworld.domain;

import java.util.Objects;
import java.util.Optional;

public record User(
        long userId,
        String email,
        String username,
        Optional<String> bio,
        Optional<String> image,
        String passwordHash
) {
    public User {
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(bio, "bio must not be null");
        Objects.requireNonNull(image, "image must not be null");
        Objects.requireNonNull(passwordHash, "passwordHash must not be null");
    }
}
