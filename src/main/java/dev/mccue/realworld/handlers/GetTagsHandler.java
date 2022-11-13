package dev.mccue.realworld.handlers;

import dev.mccue.realworld.domain.TagsResponse;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.Request;

import java.util.List;

public final class GetTagsHandler<Ctx> implements RegexRouter.HandlerTakingContext<Ctx> {

    @Override
    public IntoResponse handle(Ctx context, Request request) {
        return new TagsResponse(List.of());
    }
}
