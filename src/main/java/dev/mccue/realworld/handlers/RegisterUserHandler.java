package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.json.decode.alpha.Decoder;
import dev.mccue.json.decode.alpha.JsonDecodingException;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.domain.UserResponse;
import dev.mccue.realworld.utils.BodyUtils;
import dev.mccue.realworld.utils.Responses;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.List;

import static dev.mccue.realworld.service.UserService.RegistrationResult.*;

public final class RegisterUserHandler<Ctx extends HasUserService> implements RegexRouter.HandlerTakingContext<Ctx> {
    public record RegisterUserRequest(String email, String username, String password) {
        static RegisterUserRequest fromJson(Json json) throws JsonDecodingException {
            return Decoder.field(
                    json,
                    "user",
                    userFields -> new RegisterUserRequest(
                            Decoder.field(userFields, "email", Decoder::string),
                            Decoder.field(userFields, "username", Decoder::string),
                            Decoder.field(userFields, "password", Decoder::string)
                    ));
        }
    }

    @Override
    public IntoResponse handle(Ctx ctx, Request request) {
        var userService = ctx.userService();
        var registerUserRequest = BodyUtils.parseBody(request, RegisterUserRequest::fromJson);

        return switch (userService.register(
                registerUserRequest.username,
                registerUserRequest.email,
                registerUserRequest.password
        )) {
            case EmailTaken __ ->
                    Responses.validationError(List.of("email taken"));
            case UsernameTaken __ ->
                    Responses.validationError(List.of("username taken"));
            case Success(User user) ->
                    new UserResponse(user);
        };
    }
}
