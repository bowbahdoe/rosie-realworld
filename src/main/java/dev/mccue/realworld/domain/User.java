package dev.mccue.realworld.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record User(
        UUID userId,
        String email,
        String token,
        String username,
        String bio,
        Optional<String> image
) {
    public User {
        Objects.requireNonNull(email);
        Objects.requireNonNull(token);
        Objects.requireNonNull(username);
        Objects.requireNonNull(bio);
        Objects.requireNonNull(image);
    }
}
