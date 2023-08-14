package example.service;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.Cart;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.UserRepository;
import org.example.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.example.constants.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl service;
    @Mock
    private UserRepository repo;
    @Mock
    private CartRepository repoCart;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private Cart cart;

    @BeforeEach
    public void init() {
        cart = new Cart();
        cart.setId(1L);
        user = new User();
        user.setId(1L);
        user.setFullName("Linh Tram");
        user.setEmail("linhtram@gmail.com");
        user.setPhone("0797381985");
        user.setUsername("linhtram");
        user.setPassword("123");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setCart(cart);
        cart.setUser(user);
    }

    @Test
    public void whenRegistration_thenReturnUserObject(){
        Mockito.lenient().when(repo.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.lenient().when(passwordEncoder.matches(any(), any())).thenReturn(true);
        Mockito.lenient().when(repo.save(any())).thenReturn(user);
        Mockito.lenient().when(repoCart.save(any())).thenReturn(cart);

        service.register(user);

        assertEquals(1L, cart.getId());
        assertNotNull(cart.getUser());
        verify(repo, times(1)).findByUsername("linhtram");
        verify(repo, times(1)).save(user);
    }

    @Test
    public void whenRegistration_thenReturnUsernameInUseException() {
        Mockito.lenient().when(repo.findByUsername(any())).thenReturn(Optional.of(user));

        assertThrows(ApiRequestException.class, () -> {
            service.register(user);
        });

        verify(repo, never()).save(any(User.class));
    }

    @Test
    public void whenLogin_thenReturnUser(){
        lenient().when(repo.findByUsername(any())).thenReturn(Optional.of(user));
        lenient().when(passwordEncoder.matches(any(), any())).thenReturn(true);

        service.login("linhtram", "123");

        assertNotNull(user.getCart());
        assertNotNull(user.getId());
        assertEquals(Collections.singleton(Role.ROLE_USER), user.getRoles());
        verify(repo, times(1)).findByUsername("linhtram");
        verify(passwordEncoder, times(1)).matches(any(), any());
    }

    @Test
    public void whenLogin_thenReturnUserNotFoundException() {
        lenient().when(repo.findByUsername(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.login("linhtram", "123");
        });

        assertTrue(thrown.getMessage().contains(USER_NOT_FOUND));
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    public void whenLogin_thenReturnIncorrectPasswordException() {
        lenient().when(repo.findByUsername(any())).thenReturn(Optional.of(user));
        lenient().when(passwordEncoder.matches(any(), any())).thenReturn(false);

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.login("linhtram", "123");
        });

        assertTrue(thrown.getMessage().contains(INCORRECT_PASSWORD));
        verify(repo, times(1)).findByUsername("linhtram");
    }
}
