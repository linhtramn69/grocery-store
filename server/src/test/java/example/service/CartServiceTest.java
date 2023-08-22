package example.service;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.Cart;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.UserRepository;
import org.example.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.example.constants.ErrorMessage.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartServiceImpl service;
    @Mock
    private CartRepository repo;
    @Mock
    private UserRepository userRepo;
    private Cart cart;
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

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
    }

    @Test
    public void whenGetCartByIdUser_thenReturnCartObject() {
        Mockito.when(userRepo.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(repo.findByUser(any())).thenReturn(Optional.of(cart));

        Cart cartReturn = service.findByIdUser(1L);

        assertNotNull(cartReturn);
        assertEquals(cartReturn.getId(), cart.getId());
        assertEquals(cartReturn.getUser(), cart.getUser());
    }

    @Test
    public void whenGetCartByIdUser_thenReturnNotFoundUserException() {
        Mockito.when(userRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.findByIdUser(1L);
        });

        assertTrue(thrown.getMessage().contains(USER_NOT_FOUND));
        verify(repo, never()).findByUser(any(User.class));
    }

}
