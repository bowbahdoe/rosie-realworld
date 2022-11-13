package dev.mccue.realworld.handlers;

import dev.mccue.realworld.utils.QueryParams;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.Map;

public final class ListArticlesHandler<Ctx> implements RegexRouter.HandlerTakingContextAndRouteParams<Ctx> {

    @Override
    public IntoResponse handle(Ctx context, RouteParams routeParams, Request request) {
        var params = request.queryString()
                .map(QueryParams::parse)
                .orElse(Map.of());

        var tag = params.get("tag");
        var author = params.get("author");
        var favorited = params.get("favorited");
        var limit = params.get("limit");
        var offset = params.get("offset");

        return null;
    }
}
