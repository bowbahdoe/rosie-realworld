package dev.mccue.realworld.handlers;

import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.utils.Responses;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.Optional;

abstract class AuthenticatedHandler<Ctx extends HasAuthService>
        implements RegexRouter.HandlerTakingContextAndRouteParams<Ctx> {

    protected abstract IntoResponse handleAuthenticated(User user, Ctx ctx, RouteParams routeParams, Request request);
    private Optional<String> authTokenFromRequest(Request request) {
        var authHeader = request.headers().get("Authorization");
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

    @Override
    public final IntoResponse handle(Ctx ctx, RouteParams routeParams, Request request) {
        var authService = ctx.authService();
        var authToken = authTokenFromRequest(request).orElse(null);
        if (authToken == null) {
            return Responses.unauthenticated();
        }

        var user = authService.decodeJwt(authToken).orElse(null);
        if (user == null) {
            return Responses.unauthenticated();
        }

        return handleAuthenticated(
                user,
                ctx,
                routeParams,
                request
        );
    }
}
