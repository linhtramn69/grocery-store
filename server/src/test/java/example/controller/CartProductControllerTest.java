package example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.controller.CartProductController;
import org.example.enums.Role;
import org.example.model.*;
import org.example.service.CartProductService;
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
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CartProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CartProductController controller;
    @Mock
    private CartProductService service;

    private CartProduct cartProduct;

    @BeforeEach
    public void init() {
        TypeProduct type1 = new TypeProduct("FRUITS", "Fruits");
        TypeProduct type2 = new TypeProduct("VEGE", "Vegetables");
        Set<TypeProduct> listType = new HashSet<>();
        listType.add(type1);
        listType.add(type2);

        Product product = new Product();
        product.setId(1L);
        product.setName("Apple");
        product.setPrice(12.0);
        product.setImage("apple.png");
        product.setTypes(listType);

        User user = new User();
        user.setFullName("Nguyen Linh Tram");
        user.setEmail("linhtram@gmail.com");
        user.setPhone("0797381985");
        user.setUsername("linhtram");
        user.setPassword("123");
        user.setRoles(Collections.singleton(Role.ROLE_USER));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        CartProductKey key = new CartProductKey();
        key.setCartId(1L);
        key.setProductId(12L);
        cartProduct = new CartProduct();
        cartProduct.setId(key);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(2);
        cartProduct.setCart(cart);
    }

    @Test
    public void whenGetProductsFromCartByIdCart_thenReturnSetCardProduct() throws Exception {
        Set<CartProduct> list = Set.of(cartProduct);

        Mockito.when(service.getProductsFromCartByIdCart(1L)).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/cart-product/get-products-from-cart/{id}", "/api/cart-product/get-products-from-cart/{id}")
                .build();

        mockMvc.perform(get("/api/cart-product/get-products-from-cart/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAddProductToCart_thenReturnCartProduct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(cartProduct);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/cart-product/add-to-cart", "/api/cart-product/add-to-cart")
                .build();

        mockMvc.perform(post("/api/cart-product/add-to-cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteProductFromCart_thenNothing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(cartProduct);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/cart-product/delete-product-cart", "/api/cart-product/delete-product-cart")
                .build();

        mockMvc.perform(delete("/api/cart-product/delete-product-cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }
}
