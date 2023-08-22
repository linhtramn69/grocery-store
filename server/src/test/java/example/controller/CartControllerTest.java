package example.controller;

import org.example.controller.CartController;
import org.example.enums.Role;
import org.example.model.Cart;
import org.example.model.User;
import org.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CartController controller;
    @Mock
    private CartService service;
    private Cart cart;

    @BeforeEach
    public void init() {
        User user = new User();
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
    public void whenFindByIdUser_thenReturnCart() throws Exception {
        Mockito.when(service.findByIdUser(1L)).thenReturn(cart);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/cart/getByUser/{id}", "/api/cart/getByUser/{id}")
                .build();

        mockMvc.perform(get("/api/cart/getByUser/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
