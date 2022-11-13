package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.CreateArticleRequest;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.domain.UserResponse;
import dev.mccue.realworld.utils.BodyUtils;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.io.IOException;
import java.io.InputStreamReader;

public final class CreateArticleHandler<Ctx extends HasAuthService & HasArticleService>
        extends AuthenticatedHandler<Ctx> {
    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, RouteParams routeParams, Request request) {
        var createArticleRequest = BodyUtils.parseBody(request, CreateArticleRequest::fromJson);


        return null;
    }
}
