package dev.mccue.realworld.handlers;

import dev.mccue.realworld.domain.User;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

public interface AuthenticatedHandler<Ctx> {
    IntoResponse handleAuthenticated(User user, Ctx ctx, Request request);
}
