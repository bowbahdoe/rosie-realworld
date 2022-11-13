package dev.mccue.realworld.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.mccue.json.Json;
import dev.mccue.json.JsonReadException;
import dev.mccue.json.decode.alpha.Decoder;
import dev.mccue.json.decode.alpha.JsonDecodingException;
import dev.mccue.realworld.Env;
import dev.mccue.realworld.context.HasUserService;
import dev.mccue.realworld.domain.User;

import java.util.Optional;

public final class AuthService {
    private final UserService userService;

    public <Ctx extends HasUserService> AuthService(Ctx ctx) {
        this.userService = ctx.userService();
    }

    public Optional<User> decodeJwt(String authToken) {
        DecodedJWT decodedJWT;
        try {
            var algorithm = Algorithm.HMAC256(Env.JWT_SECRET);
            var verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(authToken);
        } catch (JWTVerificationException __) {
            return Optional.empty();
        }

        String userId;
        try {
            var json = Json.readString(decodedJWT.getPayload());
            userId = Decoder.field(json, "user_id", Decoder::string);
        } catch (JsonDecodingException | JsonReadException __) {
            return Optional.empty();
        }

        var user = userService.findById(userId).orElse(null);
        return Optional.ofNullable(user);
    }

    public String jwtForUser(User user) {
        var algorithm = Algorithm.HMAC256(Env.JWT_SECRET);
        return JWT.create()
                .withClaim("user_id", user.userId().toString())
                .sign(algorithm);
    }
}
