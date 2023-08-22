//package example.repository;
//
//import org.example.enums.Role;
//import org.example.model.*;
//import org.example.repository.CartProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Collections;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CartProductRepositoryTest {
//    @Autowired
//    private CartProductRepository repo;
//    CartProduct cartProduct1;
//    CartProduct cartProduct2;
//    Cart cart;
//
//    @BeforeEach
//    public void init(){
//        User user = new User();
//        user.setFullName("Nguyen Linh Tram");
//        user.setEmail("linhtram@gmail.com");
//        user.setPhone("0797381985");
//        user.setUsername("linhtram");
//        user.setPassword("123");
//        user.setRoles(Collections.singleton(Role.ROLE_USER));
//
//        cart = new Cart();
//        cart.setId(1L);
//        cart.setUser(user);
//
//        Product product1 = new Product();
//        product1.setId(1L);
//        product1.setName("Apple");
//        product1.setPrice(12.0);
//        product1.setImage("apple.png");
//        product1.setQuantity(2);
//
//        Product product2 = new Product();
//        product2.setId(2L);
//        product2.setName("Banana");
//        product2.setPrice(8.0);
//        product2.setImage("banana.png");
//        product2.setQuantity(5);
//
//        CartProductKey key1 = new CartProductKey();
//        key1.setCartId(1L);
//        key1.setProductId(1L);
//
//        CartProductKey key2 = new CartProductKey();
//        key2.setCartId(1L);
//        key2.setProductId(2L);
//
//        cartProduct1 = new CartProduct();
//        cartProduct1.setId(key1);
//        cartProduct1.setCart(cart);
//        cartProduct1.setProduct(product1);
//        cartProduct1.setQuantity(1);
//
//        cartProduct2 = new CartProduct();
//        cartProduct2.setId(key2);
//        cartProduct2.setCart(cart);
//        cartProduct2.setProduct(product2);
//        cartProduct2.setQuantity(1);
//    }
//
//    @Test
//    public void whenFindByCart_thenReturnCartProductSet(){
//        repo.save(cartProduct1);
//        repo.save(cartProduct2);
//
//        Set<CartProduct> setReturn =  repo.findByCart(cart);
//
//        assertNotNull(setReturn);
//        assertEquals(2, setReturn.size());
//    }
//}
