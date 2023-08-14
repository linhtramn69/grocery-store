package org.example.security.service;

import org.example.model.User;

import java.util.Map;

public interface IJwtGeneratorService {
    Map<String, String> generateToken(User user);

    String getUsernameFromJwtToken(String token);

    boolean validateJwtToken(String authToken);
}
