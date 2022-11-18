package dev.mccue.realworld.handlers;

import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.domain.UserResponse;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

public final class GetCurrentUserHandler<Ctx extends HasAuthService>
        implements AuthenticatedHandler<Ctx> {

    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, Request request) {
        return new UserResponse(user, ctx.authService().jwtForUser(user));
    }
}
