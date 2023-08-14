package org.example.service;

import org.example.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findByUsername(String username);

    User update(User user, String username);

    void delete(String username);
}
