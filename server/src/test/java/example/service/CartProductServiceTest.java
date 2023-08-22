package example.service;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.*;
import org.example.repository.CartProductRepository;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.example.service.impl.CartProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.example.constants.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartProductServiceTest {

    @InjectMocks
    CartProductServiceImpl service;
    @Mock
    CartProductRepository repo;
    @Mock
    CartRepository cartRepo;
    @Mock
    ProductRepository productRepo;
    CartProduct cartProduct;
    Cart cart;
    Product product;
    User user;
    CartProductKey key;

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

        product = new Product();
        product.setId(1L);
        product.setName("Apple");
        product.setPrice(12.0);
        product.setImage("apple.png");
        product.setQuantity(2);

        key = new CartProductKey();
        key.setCartId(1L);
        key.setProductId(1L);

        cartProduct = new CartProduct();
        cartProduct.setId(key);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(1);
    }

    @Test
    public void whenGetProductsFromCartByIdCart_thenReturnSetProductsFromCart() {
        Set<CartProduct> set = Set.of(cartProduct);

        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(repo.findByCart(cart)).thenReturn(set);

        Set<CartProduct> setReturn = service.getProductsFromCartByIdCart(1L);

        assertNotNull(setReturn);
        assertEquals(1, setReturn.size());
    }

    @Test
    public void whenGetProductsFromCartByIdCart_thenReturnSetEmpty() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));

        Set<CartProduct> setReturn = service.getProductsFromCartByIdCart(1L);

        assertEquals(0, setReturn.size());
        verify(repo, times(1)).findByCart(cart);
    }

    @Test
    public void whenGetProductsFromCartByIdCart_thenReturnNotFoundCartException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            service.getProductsFromCartByIdCart(1L);
        });

        verify(repo, never()).findByCart(any(Cart.class));
    }

    @Test
    public void whenSaveCartProduct_thenReturnCartProductInfoIsExist_thenReturnCartProductObject() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(cartProduct));
        Mockito.when(repo.getById(any())).thenReturn(cartProduct);
        Mockito.when(productRepo.getById(any())).thenReturn(product);
        Mockito.when(productRepo.save(any())).thenReturn(product);
        Mockito.when(repo.save(any())).thenReturn(cartProduct);

        CartProduct cartProductReturn = service.save(cartProduct);

        assertNotNull(cartProductReturn.getQuantity());
        assertEquals(cartProduct.getCart(), cartProductReturn.getCart());
        assertEquals(cartProduct.getProduct(), cartProductReturn.getProduct());
    }

    @Test
    public void whenSaveCartProduct_thenReturnCartProductInfoIsExist_thenReturnQuantityIsNotValidException() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Apple");
        product1.setPrice(12.0);
        product1.setImage("apple.png");
        product1.setQuantity(0);

        Mockito.when(repo.findById(any())).thenReturn(Optional.of(cartProduct));
        Mockito.when(repo.getById(any())).thenReturn(cartProduct);
        Mockito.when(productRepo.getById(any())).thenReturn(product1);

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.save(cartProduct);
        });

        assertTrue(thrown.getMessage().contains(QUANTITY_IS_NOT_VALID));
        verify(productRepo, never()).save(any(Product.class));
        verify(repo, never()).save(any(CartProduct.class));
    }

    @Test
    public void whenSaveCartProduct_thenReturnNewCartProductObject() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Apple");
        product1.setPrice(12.0);
        product1.setImage("apple.png");
        product1.setQuantity(1);

        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.of(product));
        Mockito.when(productRepo.save(any())).thenReturn(product1);
        Mockito.when(repo.save(any())).thenReturn(cartProduct);

        CartProduct cartProductReturn = service.save(cartProduct);

        assertNotNull(cartProductReturn.getQuantity());
        assertEquals(cartProduct.getCart(), cartProductReturn.getCart());
        assertEquals(cartProduct.getProduct(), cartProductReturn.getProduct());
    }

    @Test
    public void whenSaveCartProduct_thenReturnNotFoundCartException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.save(cartProduct);
        });

        assertTrue(thrown.getMessage().contains(CART_NOT_FOUND));
        verify(productRepo, never()).findById(any());
        verify(productRepo, never()).save(any(Product.class));
        verify(repo, never()).save(any(CartProduct.class));
    }

    @Test
    public void whenSaveCartProduct_thenReturnNotFoundProductException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.save(cartProduct);
        });

        assertTrue(thrown.getMessage().contains(PRODUCT_NOT_FOUND));
        verify(productRepo, never()).save(any(Product.class));
        verify(repo, never()).save(any(CartProduct.class));
    }

    @Test
    public void whenDeleteCartProduct_thenDeleteAllFromCartProduct_thenNothing() {
        CartProduct cartProduct1 = new CartProduct();
        cartProduct1.setId(key);
        cartProduct1.setCart(cart);
        cartProduct1.setProduct(product);
        cartProduct1.setQuantity(0);

        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.of(product));
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(cartProduct1));
        lenient().doNothing().when(repo).deleteById(any());
        Mockito.when(productRepo.save(any())).thenReturn(product);

        service.delete(cartProduct);

        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    public void whenDeleteCartProduct_thenMinusQuantityFromCartProduct() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.of(product));
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(cartProduct));
        Mockito.when(repo.save(any())).thenReturn(cartProduct);
        Mockito.when(productRepo.save(any())).thenReturn(product);

        service.delete(cartProduct);

        verify(repo, times(1)).save(any(CartProduct.class));
        verify(productRepo, times(1)).save(any(Product.class));
        verify(repo, never()).deleteById(any());
    }

    @Test
    public void whenDeleteCartProduct_thenReturnNotFoundCartException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.delete(cartProduct);
        });

        assertTrue(thrown.getMessage().contains(CART_NOT_FOUND));
        verify(productRepo, never()).findById(any());
        verify(repo, never()).findById(any());
        verify(repo, never()).deleteById(any());
        verify(productRepo, never()).save(any(Product.class));
        verify(repo, never()).save(any(CartProduct.class));
    }

    @Test
    public void whenDeleteCartProduct_thenReturnNotFoundProductException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.delete(cartProduct);
        });

        assertTrue(thrown.getMessage().contains(PRODUCT_NOT_FOUND));
        verify(repo, never()).findById(any());
        verify(repo, never()).deleteById(any());
        verify(productRepo, never()).save(any(Product.class));
        verify(repo, never()).save(any(CartProduct.class));
    }

    @Test
    public void whenDeleteCartProduct_thenReturnNotFoundProductInCartException() {
        Mockito.when(cartRepo.findById(any())).thenReturn(Optional.of(cart));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.of(product));
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.delete(cartProduct);
        });

        assertTrue(thrown.getMessage().contains(PRODUCT_NOT_FOUND_IN_CART));
        verify(repo, never()).deleteById(any());
        verify(productRepo, never()).save(any(Product.class));
        verify(repo, never()).save(any(CartProduct.class));
    }
}
