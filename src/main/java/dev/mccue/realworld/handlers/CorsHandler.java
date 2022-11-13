package dev.mccue.realworld.handlers;

import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

import java.util.Map;

public final class CorsHandler implements RegexRouter.Handler {
    @Override
    public IntoResponse handle(Request request) {
        return new Response(
                200,
                Map.of(
                        "Access-Control-Allow-Origin", "*",
                        "Access-Control-Allow-Headers", "*"
                ),
                Body.empty()
        );
    }
}
