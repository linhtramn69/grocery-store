package example.service;

import org.example.converter.ProductConverter;
import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.*;
import org.example.repository.ProductRepository;
import org.example.repository.TypeProductRepository;
import org.example.response.ProductResponse;
import org.example.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.example.constants.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.example.constants.ErrorMessage.TYPE_PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl service;
    @Mock
    private ProductRepository repo;
    @Mock
    private TypeProductRepository typeRepo;
    @Mock
    private ProductConverter converter;
    private Product product;
    private ProductResponse productResponse;
    private Orders orders;
    private OrderProduct orderProduct;

    @BeforeEach
    public void init(){
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

        productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setImage(product.getImage());
        productResponse.setTypes(product.getTypes());

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

        OrderProductKey key = new OrderProductKey();
        key.setOrderId(1L);
        key.setProductId(12L);

        orderProduct = new OrderProduct();
        orderProduct.setId(key);
        orderProduct.setOrder(orders);
        orderProduct.setProduct(product);
    }

    @Test
    public void whenFindAllProducts_thenReturnProductsList() {
        List<Product> list = List.of(product);

        Mockito.when(repo.findAll()).thenReturn(list);
        Mockito.when(converter.toResponse(any())).thenReturn(productResponse);

        Set<ProductResponse> setReturn = service.findAll();

        assertEquals(1, setReturn.size());
        assertNotNull(setReturn);
    }

    @Test
    public void whenGetAllProducts_thenReturnEmptyProductsList(){
        Mockito.when(repo.findAll()).thenReturn(Collections.emptyList());

        Set<ProductResponse> listReturn = service.findAll();

        assertEquals(0, listReturn.size());
    }

//    @Test
//    public void whenFindPopularProductsByYear_thenReturnProductsList(){
//        List<Orders> listO = List.of(orders);
//        List<OrderProduct> listOP = List.of(orderProduct);
//        List<Product> listP = List.of(product);
//
//        Mockito.when(repo.findOrdersByYear(anyInt())).thenReturn(listO);
//        Mockito.when(repo.findOrderProductByOrder(anyList())).thenReturn(listOP);
//        Mockito.when()
//        Mockito.when(converter.toResponse(any())).thenReturn(productResponse);
//
//        List<ProductResponse> listReturn = service.findPopularProductsByYear(2023);
//
//        assertNotNull(listReturn);
//    }

    @Test
    public void whenGetProductById_thenReturnProductObject(){
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(product));

        Product productReturn = service.findById(1L);

        assertNotNull(productReturn);
        assertEquals(productReturn.getId(), product.getId());
        assertEquals(productReturn.getName(), product.getName());
        assertEquals(productReturn.getPrice(), product.getPrice());
        assertEquals(productReturn.getImage(), product.getImage());
    }

    @Test
    public void whenGetProductById_thenReturnNotFoundProductException(){
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.findById(1L);
        });

        assertTrue(thrown.getMessage().contains(PRODUCT_NOT_FOUND));
    }

    @Test
    public void whenGetProductByIdType_thenReturnProductsList(){
        TypeProduct type1 = new TypeProduct("F&V", "Fruits & Vegetables");
        TypeProduct type2 = new TypeProduct("FRUITS", "Fruits");
        TypeProduct type3 = new TypeProduct("VEGE", "Vegetables");
        Set<TypeProduct> listType1 = new HashSet<>();
        listType1.add(type1);
        listType1.add(type2);
        Set<TypeProduct> listType2 = new HashSet<>();
        listType2.add(type1);
        listType2.add(type3);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Apple");
        product1.setPrice(12.0);
        product1.setImage("apple.png");
        product1.setTypes(listType1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Cucumber");
        product2.setPrice(8.0);
        product2.setImage("cucumber.png");
        product2.setTypes(listType2);

        Set<Product> listPro = Set.of(product1, product2);

        Mockito.when(typeRepo.findById(any())).thenReturn(Optional.of(type1));
        Mockito.when(repo.findByTypes(any())).thenReturn(listPro);

        Set<Product> listReturn = service.findByIdType("F&V");

        assertEquals(2, listReturn.size());
        assertNotNull(listReturn);
    }

    @Test
    public void whenGetProductByIdType_thenReturnNotFoundTypeException(){
        Mockito.when(typeRepo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.findByIdType("FRUITS");
        });

        verify(repo, never()).findByTypes(any());
        assertTrue(thrown.getMessage().contains(TYPE_PRODUCT_NOT_FOUND));
    }

    @Test
    public void whenGetProductByIdType_thenReturnEmptyProductsList(){
        TypeProduct type1 = new TypeProduct("F&V", "Fruits & Vegetables");

        Mockito.when(typeRepo.findById(any())).thenReturn(Optional.of(type1));
        Mockito.when(repo.findByTypes(any())).thenReturn(Collections.emptySet());

        Set<Product> listReturn = service.findByIdType("F&V");

        assertEquals(0, listReturn.size());
    }

    @Test
    public void whenSearchByKeyword_thenReturnProductsList(){
        Set<Product> list = Set.of(product);

        Mockito.when(repo.findByKeyword(any())).thenReturn(list);
        Mockito.when(converter.toResponse(any())).thenReturn(productResponse);

        Set<ProductResponse> setReturn = service.searchByKeyword("a");

        assertEquals(1, setReturn.size());
        assertNotNull(setReturn);
    }

    @Test
    public void whenSearchByKeyword_thenReturnEmptyProductsList(){
        Mockito.when(repo.findByKeyword(any())).thenReturn(Collections.emptySet());

        Set<ProductResponse> listReturn = service.searchByKeyword("e");

        assertEquals(0, listReturn.size());
    }

    @Test
    public void whenDeleteProduct_thenNothing(){
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(product));
        willDoNothing().given(repo).deleteById(any());

        service.delete(1L);

        verify(repo, times(1)).findById(any());
        verify(repo, times(1)).deleteById(any());
    }

    @Test
    public void whenDeleteType_thenReturnNotFoundProductException() {
        Mockito.lenient().when(repo.findById(any())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            service.delete(1L);
        });

        verify(repo, never()).deleteById(any());
    }
}
