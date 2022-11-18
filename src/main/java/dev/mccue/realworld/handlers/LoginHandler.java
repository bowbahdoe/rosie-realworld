package dev.mccue.realworld.handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.mccue.json.Json;
import dev.mccue.json.decode.alpha.Decoder;
import dev.mccue.realworld.context.HasAuthService;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.UserResponse;
import dev.mccue.realworld.service.AuthService;
import dev.mccue.realworld.service.UserService;
import dev.mccue.realworld.utils.BodyUtils;
import dev.mccue.realworld.utils.Responses;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.List;

public final class LoginHandler<Ctx extends HasUserService & HasAuthService>
        implements RegexRouter.HandlerTakingContext<Ctx> {
    public record LoginRequest(
            String email,
            String password
    ) {
        public static LoginRequest fromJson(Json json) {
            return Decoder.field(json, "user", user -> new LoginRequest(
                    Decoder.field(user, "email", Decoder::string),
                    Decoder.field(user, "password", Decoder::string)
            ));
        }
    }

    @Override
    public IntoResponse handle(Ctx context, Request request) {
        var userService = context.userService();
        var authService = context.authService();
        var loginRequest = BodyUtils.parseBody(request, LoginRequest::fromJson);

        var badEmailOrPassword = Responses.validationError(List.of("invalid email or password"));

        var user = userService.findByEmail(loginRequest.email).orElse(null);
        if (user == null || !user.isCorrectPassword(loginRequest.password())) {
            return badEmailOrPassword;
        }
        else {
            return new UserResponse(user, authService.jwtForUser(user));
        }
    }
}
