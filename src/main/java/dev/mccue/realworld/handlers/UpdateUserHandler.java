package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.json.decode.alpha.Decoder;
import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.PasswordHash;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.domain.UserResponse;
import dev.mccue.realworld.utils.BodyUtils;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.Optional;

public final class UpdateUserHandler<Ctx extends HasUserService & HasAuthService>
        implements AuthenticatedHandler<Ctx> {
    public record UpdateUserRequest(
       Optional<String> email,
       Optional<String> username,
       Optional<String> password,
       Optional<String> image,
       Optional<String> bio
    ) {
        public static UpdateUserRequest fromJson(Json json) {
            return new UpdateUserRequest(
                    Decoder.optionalField(json, "email", Decoder::string),
                    Decoder.optionalField(json, "username", Decoder::string),
                    Decoder.optionalField(json, "password", Decoder::string),
                    Decoder.optionalField(json, "image", Decoder::string),
                    Decoder.optionalField(json, "bio", Decoder::string)
            );
        }
    }

    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, Request request) {
        var userService = ctx.userService();
        var authService = ctx.authService();
        var updateUserRequest = BodyUtils.parseBody(request, UpdateUserRequest::fromJson);
        var updatedUser = new User(
                user.userId(),
                updateUserRequest.email.orElse(user.email()),
                updateUserRequest.username.orElse(user.username()),
                updateUserRequest.bio.or(user::bio),
                updateUserRequest.image.or(user::image),
                updateUserRequest.password
                        .map(PasswordHash::fromUnHashedPassword)
                        .orElse(user.passwordHash())
        );
        userService.update(updatedUser);

        return new UserResponse(
                updatedUser,
                authService.jwtForUser(updatedUser)
        );
    }
}
