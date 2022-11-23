package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.context.HasDB;
import dev.mccue.realworld.context.HasTagService;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.ArticleResponse;
import dev.mccue.realworld.domain.ArticleSearchQuery;
import dev.mccue.realworld.domain.ArticleSearchQueryBuilder;
import dev.mccue.realworld.service.ArticleService;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.realworld.utils.QueryParams;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public final class ListArticlesHandler<Ctx extends HasArticleService & HasUserService>
        implements RegexRouter.HandlerTakingContext<Ctx> {

    @Override
    public IntoResponse handle(Ctx context, Request request) {
        var params = request.queryString()
                .map(QueryParams::parse)
                .orElse(Map.of());

        var tag = params.get("tag");
        var author = params.get("author");
        var favorited = params.get("favorited");
        var limit = params.get("limit");
        var offset = params.get("offset");


        var queryBuilder = ArticleSearchQueryBuilder.builder(new ArticleSearchQuery());
        if (tag != null) {
            queryBuilder.tag(Optional.of(tag));
        }

        if (author != null) {
            queryBuilder.author(Optional.of(author));
        }

        if (favorited != null) {
            queryBuilder.favorited(Optional.of(favorited));
        }

        if (limit != null) {
            try {
                int limitInt = Integer.parseInt(limit);
                queryBuilder.limit(limitInt);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        if (offset != null) {
            try {
                int offsetInt = Integer.parseInt(offset);
                queryBuilder.offset(offsetInt);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        var query = queryBuilder.build();

        ArticleResponse.forQuery(
                context.articleService(),
                context.userService(),
                query
        );

        return new Response(new JsonBody(Json.objectBuilder().build()));

    }
}
