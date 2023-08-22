package example.service;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.*;
import org.example.repository.*;
import org.example.service.impl.OrdersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.example.constants.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {
    @InjectMocks
    private OrdersServiceImpl service;
    @Mock
    private OrdersRepository repo;
    @Mock
    private CartRepository cartRepo;
    @Mock
    private CartProductRepository cartProRepo;
    @Mock
    private ProductRepository productRepo;
    @Mock
    private OrderProductRepository orderProRepo;
    private Orders orders;
    private Cart cart;
    private Product product;
    private OrderProduct orderProduct;
    private  CartProduct cartProduct;

    @BeforeEach
    public void init() {
        product = new Product();
        product.setId(1L);
        product.setName("Apple");
        product.setPrice(12.0);
        product.setImage("apple.png");
        product.setQuantity(2);

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

        product = new Product();
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

        CartProductKey key1 = new CartProductKey();
        key1.setCartId(1L);
        key1.setProductId(1L);

        cartProduct = new CartProduct();
        cartProduct.setId(key1);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(1);
    }

    @Test
    public void whenFindOrderById_thenReturnOrderObject() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(orders));

        Orders ordersReturn = service.findOrderById(1L);

        assertNotNull(ordersReturn);
        assertEquals(ordersReturn.getId(), orders.getId());
        assertEquals(ordersReturn.getStatus(), orders.getStatus());
    }

    @Test
    public void whenFindOrderById_thenReturnNotFoundOrderException() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.findOrderById(1L);
        });

        assertTrue(thrown.getMessage().contains(ORDER_NOT_FOUND));
    }

    @Test
    public void whenFindAllOrders_thenReturnOrdersList(){
        List<Orders> list = List.of(orders);

        Mockito.when(repo.findAll()).thenReturn(list);

        List<Orders> listReturn = service.findAllOrders();

        assertNotNull(listReturn);
        assertEquals(1, listReturn.size());
    }

    @Test
    public void whenCheckout_thenReturnNewOrderObject(){
        Set<CartProduct> set = Set.of(cartProduct);

        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(cartProRepo.findByCart(any(Cart.class))).thenReturn(set);
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.of(product));
        Mockito.when(repo.save(any())).thenReturn(orders);
        Mockito.when(orderProRepo.save(any())).thenReturn(orderProduct);
        willDoNothing().given(cartProRepo).deleteById(any());

        Orders ordersReturn = service.checkout(orders);

        verify(repo, times(1)).save(orders);
        assertEquals(1L, ordersReturn.getId());
        assertEquals(0, ordersReturn.getStatus());
    }

    @Test
    public void whenCheckout_thenReturnNotFoundCartException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.checkout(orders);
        });

        assertTrue(thrown.getMessage().contains(CART_NOT_FOUND));
        verify(cartProRepo, never()).findByCart(any(Cart.class));
        verify(productRepo, never()).findById(any());
        verify(repo, never()).save(any(Orders.class));
        verify(orderProRepo, never()).save(any(OrderProduct.class));
        verify(cartProRepo, never()).deleteById(any());
    }

    @Test
    public void whenCheckout_thenReturnNotFoundProductInCartException() {
        Set<CartProduct> set = new HashSet<>();
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(cartProRepo.findByCart(any(Cart.class))).thenReturn(set);

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.checkout(orders);
        });

        assertTrue(thrown.getMessage().contains(PRODUCT_NOT_FOUND_IN_CART));
        verify(productRepo, never()).findById(any());
        verify(repo, never()).save(any(Orders.class));
        verify(orderProRepo, never()).save(any(OrderProduct.class));
        verify(cartProRepo, never()).deleteById(any());
    }

    @Test
    public void whenCheckout_thenReturnNotFoundProductException() {
        Set<CartProduct> set = Set.of(cartProduct);

        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(cartProRepo.findByCart(any(Cart.class))).thenReturn(set);
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.checkout(orders);
        });

        assertTrue(thrown.getMessage().contains(PRODUCT_NOT_FOUND));
        verify(repo, never()).save(any(Orders.class));
        verify(orderProRepo, never()).save(any(OrderProduct.class));
        verify(cartProRepo, never()).deleteById(any());
    }

    @Test
    public void whenUpdateOrder_thenReturnOrderObject(){
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(orders));
        Mockito.when(repo.save(any(Orders.class))).thenReturn(orders);

        Orders orderReturn = service.update(1L, 1);

        assertEquals(1L, orderReturn.getId());
        assertEquals(1, orderReturn.getStatus());

        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(orders);
    }

    @Test
    public void whenUpdateOrder_thenReturnNotFoundOrderException(){
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.update(1L, 1);
        });

        assertTrue(thrown.getMessage().contains(ORDER_NOT_FOUND));
    }
}
