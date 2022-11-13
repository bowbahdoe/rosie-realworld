package dev.mccue.realworld.domain;

import dev.mccue.json.Json;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Response;

public record ArticleResponse(
        String slug,
        String title,
        String description,
        Author author
) implements IntoResponse {
    public record Author(
            String username,
            String bio,
            String image,
            boolean following
    ) {
        public Json toJson() {
            return Json.objectBuilder()
                    .put("username", Json.of(username))
                    .put("bio", Json.of(bio))
                    .put("image", Json.of(image))
                    .put("following", Json.of(following))
                    .build();
        }
    }

    public Json toJson() {
        return Json.objectBuilder()
                .put("slug", Json.of(slug))
                .put("author", author.toJson())
                .put("title", Json.of(title))
                .put("description", Json.of(description))
                .build();
    }

    @Override
    public Response intoResponse() {
        return new Response(new JsonBody(this.toJson()));
    }
}
