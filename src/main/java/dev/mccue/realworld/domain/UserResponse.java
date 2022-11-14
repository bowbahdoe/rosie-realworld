package dev.mccue.realworld.domain;

import dev.mccue.json.Json;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Response;

public record UserResponse(
        User user,
        String token
) implements IntoResponse {
    public Json toJson() {
        return Json.objectBuilder()
                .put(
                        "user",
                        Json.objectBuilder()
                                .put("email", Json.of(user.email()))
                                .put("username", Json.of(user.username()))
                                .put("bio", Json.of(user.bio().orElse(null)))
                                .put("image", Json.of(user.image().orElse(null)))
                                .put("token", Json.of(token))
                                .build()
                )
                .build();
    }

    @Override
    public Response intoResponse() {
        return new Response(new JsonBody(this.toJson()));
    }
}
