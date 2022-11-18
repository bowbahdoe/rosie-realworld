package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

public final class FeedArticlesHandler<Ctx> implements RegexRouter.Handler {
    @Override
    public IntoResponse handle(Request request) {
        return new Response(new JsonBody(Json.objectBuilder().build()));
    }
}
