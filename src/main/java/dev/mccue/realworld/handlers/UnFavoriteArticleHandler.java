package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

public final class UnFavoriteArticleHandler<Ctx extends HasAuthService & HasArticleService>
        implements AuthenticatedHandlerTakingRouteParams<Ctx> {
    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, RouteParams routeParams, Request request) {
        var articleSlug = routeParams.namedParameter("slug").orElseThrow();


        return new Response(new JsonBody(Json.objectBuilder().build()));
    }
}
