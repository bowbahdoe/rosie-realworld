package dev.mccue.realworld.handlers;

import dev.mccue.json.Json;
import dev.mccue.realworld.context.HasTagService;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.service.TagService;
import dev.mccue.realworld.utils.JsonBody;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;
import dev.mccue.rosie.Response;


public final class GetTagsHandler<Ctx extends HasTagService> implements AuthenticatedHandler<Ctx> {

    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, Request request) {
        var tagService = ctx.tagService();
        return new Response(new JsonBody(
                Json.objectBuilder()
                        .put("tags", Json.of(
                                tagService.all()
                                        .stream()
                                        .map(TagService.Tag::name)
                                        .map(Json::of)
                                        .toList()
                        ))
                        .build()));
    }
}
