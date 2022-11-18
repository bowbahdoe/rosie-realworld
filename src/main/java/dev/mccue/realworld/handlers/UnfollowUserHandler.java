package dev.mccue.realworld.handlers;

import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.ProfileResponse;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.utils.Responses;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.regexrouter.RouteParams;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.List;

public final class UnfollowUserHandler<Ctx extends HasUserService>
        implements AuthenticatedHandlerTakingRouteParams<Ctx> {

    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, RouteParams routeParams, Request request) {
        var userService = ctx.userService();
        var userToFollow = routeParams.namedParameter("username")
                .flatMap(userService::findByUsername)
                .orElse(null);
        if (userToFollow != null) {
            userService.unfollow(user.userId(), userToFollow.userId());
            return new ProfileResponse(
                    userToFollow.username(),
                    userToFollow.bio(),
                    userToFollow.image(),
                    false
            );
        }
        else {
            return Responses.validationError(List.of("invalid user"));
        }
    }
}
