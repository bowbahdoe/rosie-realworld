package dev.mccue.realworld.context;

import dev.mccue.realworld.service.AuthService;

public interface HasAuthService {
    AuthService authService();
}
