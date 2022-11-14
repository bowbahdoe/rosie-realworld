package dev.mccue.realworld.context;

import dev.mccue.realworld.service.ArticleService;
import dev.mccue.realworld.service.AuthService;
import dev.mccue.realworld.service.UserService;
import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;

public record Context(SQLiteDataSource db) implements HasDB, HasUserService, HasAuthService, HasArticleService {
    public static Context start() {
        SQLiteDataSource db = new SQLiteDataSource();
        db.setUrl("jdbc:sqlite:test.db");
        try (var conn = db.getConnection();
             var stmt = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS "user" (
                    user_id integer primary key autoincrement,
                    password_hash text,
                    email text unique,
                    username text unique,
                    image text,
                    bio text
                );
                
                CREATE TABLE IF NOT EXISTS "follow" (
                    follow_id integer primary key autoincrement,
                    follower_user_id integer references user(user_id),
                    following_user_id integer references user(user_id)
                );
                """)) {
            stmt.execute();
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
}