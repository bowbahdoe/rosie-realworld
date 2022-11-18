package dev.mccue.realworld;


import dev.mccue.realworld.context.Context;
import dev.mccue.realworld.domain.ArticleSlug;
import org.microhttp.EventLoop;
import org.microhttp.Options;

import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var context = Context.start();
        var options = new Options()
                .withHost("0.0.0.0")
                .withPort(Integer.parseInt(Env.PORT));

        var eventLoop = new EventLoop(options, new RootMicrohttpHandler(options, context));

        eventLoop.start();
        eventLoop.join();
    }
}
