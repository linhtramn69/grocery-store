package example.controller;

import org.example.controller.OrderProductController;
import org.example.enums.Role;
import org.example.model.*;
import org.example.service.OrderProductService;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private OrderProductController controller;
    @Mock
    private OrderProductService service;
    private OrderProduct orderProduct;

    @BeforeEach
    public void init(){
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

        Orders orders = new Orders();
        orders.setId(1L);
        orders.setFullName("Linh Tram");
        orders.setAddress("123 abc");
        orders.setCreatedDate(new Date());
        orders.setReceived("6/9/2023");
        orders.setTotal(215.50);
        orders.setStatus(0);
        orders.setCart(cart);

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

        OrderProductKey key = new OrderProductKey();
        key.setOrderId(1L);
        key.setProductId(12L);

        orderProduct = new OrderProduct();
        orderProduct.setId(key);
        orderProduct.setOrder(orders);
        orderProduct.setProduct(product);
    }

    @Test
    public void whenGetOrderDetailsByIdOrder_thenReturnSetOrderProduct() throws Exception {
        Set<OrderProduct> list = Set.of(orderProduct);

        Mockito.when(service.findByIdOrder(1L)).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/order-details/getOrderDetails/{id}", "/api/order-details/getOrderDetails/{id}")
                .build();

        mockMvc.perform(get("/api/order-details/getOrderDetails/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
