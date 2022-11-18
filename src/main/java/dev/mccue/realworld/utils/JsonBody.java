package dev.mccue.realworld.utils;

import dev.mccue.json.Json;
import dev.mccue.rosie.Body;

import java.io.OutputStream;
import java.util.Optional;

public record JsonBody(Json value) implements Body {
    @Override
    public void writeToStream(OutputStream outputStream) {
        Body.fromString(Json.writeString(value)).writeToStream(outputStream);
    }

    @Override
    public Optional<String> defaultContentType() {
        return Optional.of("application/json; charset=utf-8");
    }
}
