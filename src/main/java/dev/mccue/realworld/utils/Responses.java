package dev.mccue.realworld.utils;

import dev.mccue.json.Json;
import dev.mccue.rosie.Response;

import java.util.List;

public final class Responses {
    private Responses() {}

    public static Response validationError(List<String> messages) {
        return new Response(
                422,
                new JsonBody(
                        Json.objectBuilder()
                                .put("errors", Json.objectBuilder()
                                        .put("body", Json.arrayBuilder()
                                                .addAll(messages.stream().map(Json::of).toList())
                                                .build())
                                        .build())
                                .build()
                )
        );
    }

    public static Response unauthenticated() {
        return new Response(
                401,
                new JsonBody(
                        Json.objectBuilder()
                                .put("errors", Json.objectBuilder()
                                        .put("body", Json.arrayBuilder()
                                                .add(Json.of("unauthenticated"))
                                                .build())
                                        .build())
                                .build()
                )
        );
    }

    public static Response internalError() {
        return new Response(
                500,
                new JsonBody(
                        Json.of("internal error")
                )
        );
    }
}
