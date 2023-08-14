package org.example.controller;

import org.example.model.User;
import org.example.security.service.JwtGeneratorService;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtGeneratorService jwtGenerator;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User newUser = authService.register(user);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) throws Exception {

        if (user.getUsername() != null && user.getPassword() != null) {
            User userResult = authService.login(user.getUsername(), user.getPassword());

            // Xác thực từ username và password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(jwtGenerator.generateToken(userResult), HttpStatus.OK);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username hoac password khong duoc trong");
        }

    }
}
