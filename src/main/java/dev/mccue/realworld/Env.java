package dev.mccue.realworld;

import java.util.Objects;

public final class Env {
    private Env() {}

    private static String env(String envVarName) {
        return Objects.requireNonNull(
                System.getenv(envVarName),
                envVarName + " not provided"
        );
    }

    public static final String PORT = env("PORT");

    public static final String ENVIRONMENT = env("ENVIRONMENT");
    public static final String JWT_SECRET = env("JWT_SECRET");

    public static boolean development() {
        return ENVIRONMENT.equalsIgnoreCase("development");
    }
}
