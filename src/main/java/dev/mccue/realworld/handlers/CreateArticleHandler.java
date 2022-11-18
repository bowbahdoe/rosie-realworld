package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.json.decode.alpha.Decoder;
import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.ArticleResponse;
import dev.mccue.realworld.domain.ExternalId;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.utils.BodyUtils;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.List;

public final class CreateArticleHandler<Ctx extends HasArticleService & HasUserService>
        implements AuthenticatedHandler<Ctx> {
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
                    Decoder.optionalField(
                            article,
                            "tagList",
                            tagList -> Decoder.array(tagList, Decoder::string),
                            List.of()
                    )
            ));
        }
    }

    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, Request request) {
        var createArticleRequest = BodyUtils.parseBody(request, CreateArticleRequest::fromJson);
        var articleService = ctx.articleService();
        var userService = ctx.userService();

        var articleId = articleService.createArticle(
                user.userId(),
                createArticleRequest.title(),
                createArticleRequest.description(),
                createArticleRequest.body(),
                createArticleRequest.tagList(),
                ExternalId.generate()
        );


        return ArticleResponse
                .forArticleId(articleService, userService, user.userId(), articleId)
                .orElseThrow();
    }
}
