package dev.mccue.realworld.utils;

import dev.mccue.json.Json;
import dev.mccue.rosie.Body;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.Optional;

public record JsonBody(Json value) implements Body {
    @Override
    public void writeToStream(OutputStream outputStream) {
        try {
            Json.write(value, new OutputStreamWriter(outputStream));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Optional<String> defaultContentType() {
        return Optional.of("application/json; charset=utf-8");
    }
}
