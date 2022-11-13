package dev.mccue.realworld.service;

import dev.mccue.realworld.context.HasDB;
import org.sqlite.SQLiteDataSource;

public final class ArticleService {
    private final SQLiteDataSource db;

    public <Ctx extends HasDB> ArticleService(Ctx ctx) {
        this.db = ctx.db();
    }
}
