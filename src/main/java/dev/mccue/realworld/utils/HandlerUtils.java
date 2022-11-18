package dev.mccue.realworld.utils;

import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.handlers.AuthenticatedHandler;
import dev.mccue.realworld.handlers.AuthenticatedHandlerTakingRouteParams;
import dev.mccue.realworld.service.AuthService;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.Request;

import java.util.Optional;

public final class HandlerUtils {
    private static Optional<String> authTokenFromRequest(Request request) {
        var authHeader = request.headers().get("authorization");

        if (authHeader == null) {
            return Optional.empty();
        }
        else {
            var split = authHeader.split(" ");
            if (split.length != 2 || !"Token".equalsIgnoreCase(split[0])) {
                return Optional.empty();
            }
            else {
                return Optional.of(split[1]);
            }
        }
    }

    private static Optional<User> userForRequest(AuthService authService, Request request) {
        var authToken = authTokenFromRequest(request).orElse(null);
        if (authToken == null) {
            return Optional.empty();
        }

        var user = authService.decodeJwt(authToken).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        return Optional.of(user);
    }


    public static <Ctx extends HasAuthService> RegexRouter.HandlerTakingContext<Ctx> authenticated(
                AuthenticatedHandler<Ctx> handler
    ) {
        return (ctx, request) ->
                userForRequest(ctx.authService(), request)
                        .map(user -> handler.handleAuthenticated(user, ctx, request))
                        .orElse(Responses.unauthenticated());
    }

    public static <Ctx extends HasAuthService> RegexRouter.HandlerTakingContextAndRouteParams<Ctx> authenticated(
            AuthenticatedHandlerTakingRouteParams<Ctx> handler
    ) {
        return (ctx, routeParams, request) ->
                userForRequest(ctx.authService(), request)
                        .map(user -> handler.handleAuthenticated(user, ctx, routeParams, request))
                        .orElse(Responses.unauthenticated());
    }
}
