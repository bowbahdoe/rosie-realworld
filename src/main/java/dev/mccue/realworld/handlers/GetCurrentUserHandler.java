package dev.mccue.realworld.handlers;

import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.domain.UserResponse;
import dev.mccue.realworld.service.AuthService;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

public final class GetCurrentUserHandler<Ctx extends HasAuthService>
        extends AuthenticatedHandler<Ctx> {

    @Override
    protected IntoResponse handleAuthenticated(User user, Ctx ctx, RouteParams routeParams, Request request) {
        return new UserResponse(user, ctx.authService().jwtForUser(user));
    }
}
