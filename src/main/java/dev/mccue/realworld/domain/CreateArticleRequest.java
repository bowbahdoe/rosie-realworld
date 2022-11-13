package dev.mccue.realworld.domain;

import dev.mccue.json.Json;
import dev.mccue.json.decode.alpha.Decoder;

import java.util.List;

public record CreateArticleRequest(
        String title,
        String description,
        String body,
        List<String> tagList
) {
    public static CreateArticleRequest fromJson(Json json) {
        return Decoder.field(json, "article", article -> new CreateArticleRequest(
                Decoder.field(article, "title", Decoder::string),
                Decoder.field(article, "description", Decoder::string),
                Decoder.field(article, "body", Decoder::string),
                Decoder.field(article, "tagList", tagList -> Decoder.array(tagList, Decoder::string))
        ));
    }
}
