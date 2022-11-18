package dev.mccue.realworld.handlers;

import dev.mccue.realworld.domain.TagsResponse;
import dev.mccue.realworld.domain.User;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.List;

public final class GetTagsHandler<Ctx> implements AuthenticatedHandler<Ctx> {


    @Override
    public IntoResponse handleAuthenticated(User user, Ctx ctx, Request request) {
        return new TagsResponse(List.of());
    }
}
