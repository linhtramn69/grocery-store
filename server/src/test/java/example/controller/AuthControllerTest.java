package example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.controller.AuthController;
import org.example.enums.Role;
import org.example.model.User;
import org.example.security.service.JwtGeneratorService;
import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.crypto.SecretKey;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    @Mock
    private JwtGeneratorService jwtGenerator;
    @Mock
    private AuthenticationManager authenticationManager;
    @Autowired
    private MockMvc mockMvc;
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${app.jwttoken.message}")
    private String message;
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setFullName("Nguyen Linh Tram");
        user.setEmail("linhtram@gmail.com");
        user.setPhone("0797381985");
        user.setUsername("linhtram");
        user.setPassword("123");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
    }

    @Test
    public void whenRegistration_thenReturnUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .addPlaceholderValue("/api/auth/register", "/api/auth/register")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void whenLogin_thenReturnUserInDB() throws Exception {

        // Setup our mocked service
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("linhtram", "123"));

        String jwtToken = Jwts.builder()
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
        jwtTokenGen.put("message", message);
        jwtTokenGen.put("username", user.getUsername());
        jwtTokenGen.put("role", roles.toString());

        lenient().when(authService.login(any(), any())).thenReturn(user);
        lenient().when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("linhtram", "123"))).thenReturn(authentication);
        lenient().when(jwtGenerator.generateToken(user)).thenReturn(jwtTokenGen);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .addPlaceholderValue("/api/auth/login", "/api/auth/login")
                .build();

        // Execute the POST request
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                // Validate the response code
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
