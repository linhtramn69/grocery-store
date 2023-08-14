package org.example.service.impl;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.exception.PasswordException;
import org.example.model.Cart;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static org.example.constants.ErrorMessage.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        Optional<User> userDB = userRepository.findByUsername(user.getUsername());
        if (userDB.isPresent()) {
            throw new ApiRequestException(USERNAME_IN_USE, HttpStatus.BAD_REQUEST);
        }
        String encryptedPasswordEncoder = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPasswordEncoder);
        if (user.getRoles() == null) {
            user.setRoles(Collections.singleton(Role.ROLE_USER));
        } else {
            user.setRoles(Collections.singleton(Role.ROLE_ADMIN));
        }
        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return user;
    }

    @Override
    public User login(String username, String password) {
        User userDB = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (!passwordEncoder.matches(password, userDB.getPassword())) {
            throw new ApiRequestException(INCORRECT_PASSWORD, HttpStatus.NOT_FOUND);
        }
        return userDB;
    }
}
