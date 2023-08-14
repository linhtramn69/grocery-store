package org.example.service;

import org.example.model.User;

public interface AuthService {
    User register(User user);

    User login(String username, String password);
}
