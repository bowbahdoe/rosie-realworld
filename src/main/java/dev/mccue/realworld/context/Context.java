package dev.mccue.realworld.context;

import dev.mccue.realworld.service.ArticleService;
import dev.mccue.realworld.service.AuthService;
import dev.mccue.realworld.service.TagService;
import dev.mccue.realworld.service.UserService;
import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;

public record Context(SQLiteDataSource db) implements HasDB, HasUserService, HasAuthService, HasArticleService, HasTagService {
    public static Context start() {
        SQLiteDataSource db = new SQLiteDataSource();
        db.setUrl("jdbc:sqlite:test.db");
        try (var conn = db.getConnection()) {
            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS "user" (
                        user_id integer primary key autoincrement,
                        password_hash text,
                        email text unique,
                        username text unique,
                        image text,
                        bio text
                    );
                    """)) {
                stmt.execute();
            }

            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS follow (
                        follow_id integer primary key autoincrement,
                        follower_user_id integer references user(user_id),
                        following_user_id integer references user(user_id)
                    );
                    """)) {
                stmt.execute();
            }

            // language=SQL
            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS article (
                        article_id integer primary key autoincrement,
                        external_article_id text unique,
                        title text,
                        description text,
                        body text,
                        created_at datetime not null default current_timestamp,
                        updated_at datetime not null default current_timestamp,
                        user_id integer references user(user_id)
                    );
                    """)) {
                stmt.execute();
            }

            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS tag (
                        tag_id integer primary key autoincrement,
                        name text unique
                    );
                    """)) {
                stmt.execute();
            }

            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS article_tag (
                        article_id integer references article(article_id),
                        tag_id integer references tag(tag_id)
                    );
                    """)) {
                stmt.execute();
            }

            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS article_favorite (
                        article_id integer references article(article_id),
                        user_id integer references user(user_id)
                    );
                    """)) {
                stmt.execute();
            }

            try(var stmt = conn.prepareStatement(
                    // language=SQL
                    """
                    CREATE TABLE IF NOT EXISTS comment (
                        comment_id integer primary key autoincrement,
                        body text,
                        article_id integer references article(article_id),
                        user_id integer references user(user_id),
                        created_at datetime,
                        updated_at datetime
                    );
                    """)) {
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Context(db);
    }

    @Override
    public UserService userService() {
        return new UserService(this);
    }

    @Override
    public AuthService authService() {
        return new AuthService(this);
    }

    @Override
    public ArticleService articleService() {
        return new ArticleService(this);
    }

    @Override
    public TagService tagService() {
        return new TagService(this);
    }
}