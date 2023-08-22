package example.service;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.*;
import org.example.repository.OrderProductRepository;
import org.example.repository.OrdersRepository;
import org.example.service.OrderProductService;
import org.example.service.impl.OrderProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderProductServiceTest {
    @InjectMocks
    private OrderProductServiceImpl service;
    @Mock
    private OrderProductRepository repo;
    @Mock
    private OrdersRepository ordersRepo;
    private OrderProduct orderProduct;
    private Orders orders;

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

        orders = new Orders();
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
    public void whenFindByIdOrder_thenReturnOrderProductObject(){
        Set<OrderProduct> set = Set.of(orderProduct);

        Mockito.when(ordersRepo.findById(any())).thenReturn(Optional.of(orders));
        Mockito.when(repo.findByOrder(any(Orders.class))).thenReturn(set);

        Set<OrderProduct> setReturn = service.findByIdOrder(1L);

        assertNotNull(setReturn);
        assertEquals(1, setReturn.size());
    }

    @Test
    public void whenFindByIdOrder_thenReturnNotFoundOrderException(){
        Mockito.when(ordersRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            service.findByIdOrder(1L);
        });

        verify(repo, never()).findByOrder(any(Orders.class));
    }
}
