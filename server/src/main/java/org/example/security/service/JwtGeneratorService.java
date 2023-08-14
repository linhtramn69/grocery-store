package org.example.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.enums.Role;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtGeneratorService implements IJwtGeneratorService {
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Logger logger = LoggerFactory.getLogger(JwtGeneratorService.class);

    @Override
    public Map<String, String> generateToken(User user) {
        String jwtToken = "";
        jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        Set<Role> roles = new HashSet<>();
        user.getRoles().forEach(item -> {
            roles.add(item);
        });
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("id", String.valueOf(user.getId()));
        jwtTokenGen.put("username", user.getUsername());
        jwtTokenGen.put("role", roles.toString());
        return jwtTokenGen;
    }

    @Override
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
