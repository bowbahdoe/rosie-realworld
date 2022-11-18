package dev.mccue.realworld.domain;

import dev.mccue.json.Json;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Response;

import java.util.Optional;

public record ProfileResponse(
        String username,
        Optional<String> bio,
        Optional<String> image,
        boolean following
) implements IntoResponse {
    public Json toJson() {
        return Json.objectBuilder()
                .put("profile", Json.objectBuilder()
                        .put("username", Json.of(username))
                        .put("bio", Json.of(bio.orElse(null)))
                        .put("image", Json.of(image.orElse(null)))
                        .put("following", Json.of(following))
                        .build())
                .build();
    }

    @Override
    public Response intoResponse() {
        return new Response(new JsonBody(this.toJson()));
    }
}
