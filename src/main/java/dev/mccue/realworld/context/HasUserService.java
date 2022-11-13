package dev.mccue.realworld.context;

import dev.mccue.realworld.service.UserService;

public interface HasUserService {
    UserService userService();
}
