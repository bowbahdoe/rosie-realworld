package dev.mccue.realworld.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.context.HasDB;
import org.sqlite.SQLiteDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public final class UserService {
    private final SQLiteDataSource db;


    public <T extends HasDB> UserService(T context) {
        this.db = context.db();
    }

    private static final String SELECT_FIELDS = """
            "user".user_id, "user".email, "user".username, "user".bio, "user".image, "user".password_hash
            """;

    private static User userFromRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong(1),
                rs.getString(2),
                rs.getString(3),
                Optional.ofNullable(rs.getString(4)),
                Optional.ofNullable(rs.getString(5)),
                rs.getString(6)
        );
    }

    public sealed interface RegistrationResult {
        record EmailTaken() implements RegistrationResult {

        }

        record UsernameTaken() implements RegistrationResult {

        }

        record Success(User user) implements RegistrationResult {
        }
    }

    public RegistrationResult register(
            String username,
            String email,
            String password
    ) {
        username = username.toLowerCase();
        email = email.toLowerCase();

        try (var conn = this.db.getConnection()) {
            conn.setAutoCommit(false);
            try (var selectByEmail = conn.prepareStatement("""
                    SELECT 1
                    FROM "user"
                    WHERE "user".email = ?
                    """)) {
                selectByEmail.setString(1, email);
                if (selectByEmail.executeQuery().next()) {
                    return new RegistrationResult.EmailTaken();
                }
            }

            try (var selectByUsername = conn.prepareStatement("""
                    SELECT 1
                    FROM "user"
                    WHERE "user".username = ?
                    """)) {
                selectByUsername.setString(1, username);
                if (selectByUsername.executeQuery().next()) {
                    return new RegistrationResult.UsernameTaken();
                }
            }

            try (var insert = conn.prepareStatement("""
                    INSERT INTO "user"(username, email, password_hash)
                    VALUES (?, ?, ?)
                    """)) {
                insert.setString(1, username);
                insert.setString(2, email);
                insert.setString(3, BCrypt.withDefaults().hashToString(12, password.toCharArray()));
                insert.execute();
            }


            conn.commit();

            try (var findByEmail = conn.prepareStatement("""
                    SELECT %s
                    FROM "user"
                    WHERE "user".email = ?
                    """.formatted(SELECT_FIELDS))) {
                findByEmail.setString(1, email);

                var rs = findByEmail.executeQuery();
                rs.next();
                var user = userFromRow(rs);

                return new RegistrationResult.Success(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findById(long userId) {
        try (var conn = this.db.getConnection();
             var stmt = conn.prepareStatement("""
                     SELECT %s
                     FROM "user"
                     WHERE "user".user_id = ?
                     """.formatted(SELECT_FIELDS))) {
            stmt.setLong(1, userId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(userFromRow(rs));
            }
            else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByEmail(String email) {
        try (var conn = this.db.getConnection();
             var stmt = conn.prepareStatement("""
                     SELECT %s
                     FROM "user"
                     WHERE "user".email = ?
                     """.formatted(SELECT_FIELDS))) {
            stmt.setString(1, email);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(userFromRow(rs));
            }
            else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user) {
        try (var conn = this.db.getConnection();
             var update = conn.prepareStatement("""
                    UPDATE "user"
                    SET "user".username = ?, "user".email = ?, "user".bio = ?, "user".image = ?, "user".password_hash = ?
                    WHERE "user".user_id = ?
                    """)) {
            update.setString(1, user.username());
            update.setString(2, user.email());
            update.setString(3, user.bio().orElse(null));
            update.setString(4, user.image().orElse(null));
            update.setString(5, user.passwordHash());
            update.setLong(6, user.userId());
            System.out.println(update);
            update.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
