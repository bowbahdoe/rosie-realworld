package dev.mccue.realworld.domain;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.Objects;

public record PasswordHash(String value) {
    public PasswordHash {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static PasswordHash fromUnHashedPassword(String password) {
        return new PasswordHash(
                BCrypt.withDefaults().hashToString(12, password.toCharArray())
        );
    }

    public boolean isCorrectPassword(String password) {
        return BCrypt.verifyer()
                .verify(password.toCharArray(), this.value)
                .verified;
    }
}
