package dev.mccue.realworld.service;

import dev.mccue.realworld.domain.User;
import dev.mccue.realworld.context.HasDB;

import java.util.Optional;
import java.util.UUID;

public final class UserService {
    private final DB db;

    public <T extends HasDB> UserService(T context) {
        this.db = context.db();
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
        return this.db.operate(state -> {
            var emailTaken = state.users()
                    .values()
                    .stream()
                    .anyMatch(user -> user.username().equalsIgnoreCase(email));

            if (emailTaken) {
                return new RegistrationResult.UsernameTaken();
            }

            var usernameTaken = state.users()
                    .values()
                    .stream()
                    .anyMatch(user -> user.username().equalsIgnoreCase(username));

            if (usernameTaken) {
                return new RegistrationResult.UsernameTaken();
            }

            var user = new User(
                    UUID.randomUUID(),
                    email,
                    "",
                    username,
                    "",
                    Optional.empty()
            );

            state.users().put(user.userId().toString(), user);

            return new RegistrationResult.Success(user);
        });
    }

    public Optional<User> findById(String userId) {
        return this.db.operate(state -> {
            return Optional.ofNullable(state.users().get(userId));
        });
    }
}
