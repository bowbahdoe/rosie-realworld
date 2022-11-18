package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;

public final class DeleteArticleHandler<Ctx> implements AuthenticatedHandler<Ctx> {

    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, Request request) {
        return new Response(new JsonBody(Json.objectBuilder().build()));
    }
}
