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
            "user".user_id, "user".email, "user".token, "user".username, "user".password_hash
            """;

    private static User userFromRow(ResultSet rs) throws SQLException {
        return new User(
                UUID.fromString(rs.getString(1)),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                Optional.ofNullable(rs.getString(1))
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
            }

            try (var findByEmail = conn.prepareStatement("""
                    SELECT %s
                    FROM "user"
                    WHERE "user".email = ?
                    """.formatted(SELECT_FIELDS))) {
                findByEmail.setString(1, email);
                var rs = findByEmail.executeQuery();

                rs.next();
                var user = userFromRow(rs);

                conn.commit();

                return new RegistrationResult.Success(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findById(String userId) {
        try (var conn = this.db.getConnection();
             var stmt = conn.prepareStatement("""
                     SELECT %s
                     FROM "user"
                     WHERE "user".user_id = ?
                     """.formatted(SELECT_FIELDS))) {
            stmt.setString(1, userId);
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
}
