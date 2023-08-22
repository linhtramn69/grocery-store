package example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.controller.OrdersController;
import org.example.enums.Role;
import org.example.model.Cart;
import org.example.model.Orders;
import org.example.model.User;
import org.example.response.TotalResponse;
import org.example.service.OrdersService;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrdersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private OrdersController controller;
    @Mock
    private OrdersService service;

    private Orders orders;

    @BeforeEach
    public void init() {
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

        orders = new Orders();
        orders.setId(1L);
        orders.setFullName("Linh Tram");
        orders.setAddress("123 abc");
        orders.setCreatedDate(new Date());
        orders.setReceived("6/9/2023");
        orders.setTotal(215.50);
        orders.setStatus(0);
        orders.setCart(cart);
    }

    @Test
    public void whenGetAllOrders_thenReturnListOrder() throws Exception {
        List<Orders> list = List.of(orders);

        Mockito.when(service.findAllOrders()).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/orders/getAll", "/api/orders/getAll")
                .build();

        mockMvc.perform(get("/api/orders/getAll")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetOrderById_thenReturnOrder() throws Exception {
        Mockito.when(service.findOrderById(1L)).thenReturn(orders);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/orders/getOrder/{id}", "/api/orders/getOrder/{id}")
                .build();

        mockMvc.perform(get("/api/orders/getOrder/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCheckout_thenReturnNewOrder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(orders);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/orders/checkout", "/api/orders/checkout")
                .build();

        mockMvc.perform(post("/api/orders/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateStatusOrder_thenReturnOrder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(orders);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/orders/update-status/{id}", "/api/orders/update-status/{id}")
                .build();

        mockMvc.perform(patch("/api/orders/update-status/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetTotalByYear_thenReturnListTotalByMonth() throws Exception {
        TotalResponse r1 = new TotalResponse(1, 0.0);
        TotalResponse r2 = new TotalResponse(1, 0.0);
        TotalResponse r3 = new TotalResponse(1, 0.0);
        TotalResponse r4 = new TotalResponse(1, 0.0);
        TotalResponse r5 = new TotalResponse(1, 0.0);
        TotalResponse r6 = new TotalResponse(1, 0.0);
        TotalResponse r7 = new TotalResponse(1, 0.0);
        TotalResponse r8 = new TotalResponse(1, 0.0);
        TotalResponse r9 = new TotalResponse(1, 0.0);
        TotalResponse r10 = new TotalResponse(1, 0.0);
        TotalResponse r11 = new TotalResponse(1, 0.0);
        TotalResponse r12 = new TotalResponse(1, 0.0);
        List<TotalResponse> list = List.of(r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12);

        Mockito.when(service.getTotalByYear(2023)).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/orders/getTotalByYear/{year}", "/api/orders/getTotalByYear/{year}")
                .build();

        mockMvc.perform(get("/api/orders/getTotalByYear/{year}", 2023)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}
