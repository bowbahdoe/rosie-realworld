package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.service.ArticleService;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.realworld.utils.QueryParams;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

import java.util.Map;

public final class ListArticlesHandler<Ctx extends HasArticleService>
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

        return new Response(new JsonBody(Json.objectBuilder().build()));
    }
}
