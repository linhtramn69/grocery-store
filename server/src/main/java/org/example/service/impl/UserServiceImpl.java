package org.example.service.impl;

import org.example.exception.ApiRequestException;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.constants.ErrorMessage.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public User update(User user, String username) {
        User userDB = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        String encryptedPasswordEncoder = passwordEncoder.encode(user.getPassword());
        userDB.setPassword(encryptedPasswordEncoder);
        userDB.setUsername(user.getUsername());
        userDB.setPassword(user.getPassword());
        userDB.setFullName(user.getFullName());
        userDB.setEmail(user.getEmail());
        userDB.setPhone(user.getPhone());
        return userRepository.save(userDB);
    }

    @Override
    public void delete(String username) {
        User userDB = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        userRepository.delete(userDB);
    }
}
