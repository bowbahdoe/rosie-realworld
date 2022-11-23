package dev.mccue.realworld;


import dev.mccue.realworld.context.Context;
import dev.mccue.realworld.handlers.*;
import dev.mccue.realworld.utils.HandlerUtils;
import dev.mccue.realworld.utils.Responses;
import dev.mccue.regexrouter.RegexRouter;
import dev.mccue.rosie.Body;
import dev.mccue.rosie.IntoResponse;
import dev.mccue.rosie.microhttp.MicrohttpAdapter;
import org.microhttp.Handler;
import org.microhttp.Options;
import org.microhttp.Request;
import org.microhttp.Response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static dev.mccue.realworld.utils.HandlerUtils.authenticated;

final class RootMicrohttpHandler implements Handler {

    private final Options options;
    private final Context context;
    private final RegexRouter<Context> router;

    RootMicrohttpHandler(Options options, Context context) {
        this.options = options;
        this.context = context;
        this.router = RegexRouter.<Context>builder()
                .addMapping(
                        "OPTIONS",
                        Pattern.compile(".+"),
                        new CorsHandler()
                )
                .addMapping(
                        "POST",
                        Pattern.compile("/api/users/login"),
                        new LoginHandler<>()
                )
                .addMapping(
                        "POST",
                        Pattern.compile("/api/users"),
                        new RegisterUserHandler<>()
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/user"),
                        authenticated(new GetCurrentUserHandler<>())
                )
                .addMapping(
                        "PUT",
                        Pattern.compile("/api/user"),
                        authenticated(new UpdateUserHandler<>())
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/profiles/(?<username>.+)"),
                        new GetProfileHandler<>()
                )
                .addMapping(
                        "POST",
                        Pattern.compile("/api/profiles/(?<username>.+)/follow"),
                        authenticated(new FollowUserHandler<>())
                )
                .addMapping(
                        "DELETE",
                        Pattern.compile("/api/profiles/(?<username>.+)/follow"),
                        authenticated(new UnfollowUserHandler<>())
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/articles"),
                        new ListArticlesHandler<>()
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/articles/feed"),
                        new FeedArticlesHandler<>()
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/articles/(?<slug>.+)"),
                        new GetArticleHandler<>()
                )
                .addMapping(
                        "POST",
                        Pattern.compile("/api/articles"),
                        authenticated(new CreateArticleHandler<>())
                )
                .addMapping(
                        "PUT",
                        Pattern.compile("/api/articles/(?<slug>.+)"),
                        authenticated(new UpdateArticleHandler<>())
                )
                .addMapping(
                        "DELETE",
                        Pattern.compile("/api/articles/(?<slug>.+)"),
                        authenticated(new DeleteArticleHandler<>())
                )
                .addMapping(
                        "POST",
                        Pattern.compile("/api/articles/(?<slug>.+)/comments"),
                        new AddArticleCommentHandler<>()
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/articles/(?<slug>.+)/comments"),
                        new GetArticleCommentsHandler<>()
                )
                .addMapping(
                        "DELETE",
                        Pattern.compile("/api/articles/(?<slug>.+)/comments/(?<id>.+)"),
                        new DeleteCommentHandler<>()
                )
                .addMapping(
                        "POST",
                        Pattern.compile("/api/articles/(?<slug>.+)/favorite"),
                        authenticated(new FavoriteArticleHandler<>())
                )
                .addMapping(
                        "DELETE",
                        Pattern.compile("/api/articles/(?<slug>.+)/favorite"),
                        authenticated(new UnFavoriteArticleHandler<>())
                )
                .addMapping(
                        "GET",
                        Pattern.compile("/api/tags"),
                        authenticated(new GetTagsHandler<>())
                )
                .build();
    }

    @Override
    public void handle(Request request, Consumer<Response> consumer) {
        Thread.startVirtualThread(() -> {
            IntoResponse rosieResponse;
            try {
                var rosieRequest = MicrohttpAdapter.fromMicrohttpRequest(
                        options.host(),
                        options.port(),
                        request
                );
                rosieResponse = router.handle(context, rosieRequest).orElse(
                        new dev.mccue.rosie.Response(404)
                );
            } catch (Throwable t) {
                if (t instanceof IntoResponse exceptionResponse) {
                    rosieResponse = exceptionResponse.intoResponse();
                }
                else {
                    if (Env.development()) {
                        var sw = new StringWriter();
                        t.printStackTrace(new PrintWriter(sw));
                        System.err.println(sw);
                        rosieResponse = new dev.mccue.rosie.Response(500, Body.fromString(sw.toString()));
                    }
                    else {
                        rosieResponse = Responses.internalError();
                    }

                }
            }

            Response response = MicrohttpAdapter.toMicrohttpResponse(Responses.internalError());
            try {
                response = MicrohttpAdapter.toMicrohttpResponse(rosieResponse.intoResponse());
            } finally {
                consumer.accept(response);
            }
        });
    }
}
