package dev.mccue.realworld.domain;

import dev.mccue.json.Json;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Response;

import java.util.List;

public record TagsResponse(List<String> tags) implements IntoResponse {
    public TagsResponse(List<String> tags) {
        this.tags = List.copyOf(tags);
    }

    @Override
    public Response intoResponse() {
        return new Response(new JsonBody(
                Json.objectBuilder()
                        .put("tags", Json.arrayBuilder()
                                .build())
                        .build()
        ));
    }
}
