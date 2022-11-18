package dev.mccue.realworld.domain;

import dev.mccue.realworld.repository.SettableParameter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public record ExternalId(String value) {
    public ExternalId {
        Objects.requireNonNull(value, "external id value should not be null.");
        if (!URLEncoder.encode(value, StandardCharsets.UTF_8).equals(value)) {
            throw new IllegalArgumentException("external ids should be url safe. %s".formatted(value));
        }
    }

    private static final int DEFAULT_LENGTH = 8;

    public static ExternalId generate() {
        return generate(DEFAULT_LENGTH);
    }

    public static ExternalId generate(long length) {
        return generate(ThreadLocalRandom.current(), length);
    }

    public static ExternalId generate(Random random) {
        return generate(random, DEFAULT_LENGTH);
    }

    public static ExternalId generate(Random random, long length) {
        if (length <= 0) {
            throw new IllegalArgumentException("external id needs to have a positive length");
        }
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return new ExternalId(sb.toString());
    }
}
