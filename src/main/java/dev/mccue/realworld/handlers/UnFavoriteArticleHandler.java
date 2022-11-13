package dev.mccue.realworld.handlers;

import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.domain.User;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

public final class UnFavoriteArticleHandler<Ctx extends HasAuthService & HasArticleService>
        extends AuthenticatedHandler<Ctx> {
    @Override
    protected IntoResponse handleAuthenticated(User user, Ctx ctx, RouteParams routeParams, Request request) {
        var articleId = routeParams.namedParameter("slug").orElseThrow();


        return null;
    }
}
