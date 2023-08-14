package example.controller;

import org.example.controller.ProductController;
import org.example.converter.ProductConverter;
import org.example.model.Product;
import org.example.model.TypeProduct;
import org.example.response.ProductResponse;
import org.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductConverter converter;
    @InjectMocks
    private ProductController controller;
    @Mock
    private ProductService service;

    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    public void init() {
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
    }

    @Test
    public void whenGetAllProduct_thenReturnSetProductResponse() throws Exception {
        Set<ProductResponse> list = Set.of(productResponse);

        Mockito.when(service.findAll()).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/getAll", "/api/product/getAll")
                .build();

        mockMvc.perform(get("/api/product/getAll")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetPopularProductsByYear_thenReturnListProductResponse() throws Exception {
        List<ProductResponse> list = List.of(productResponse);

        Mockito.when(service.findPopularProductsByYear(2023)).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/getPopularProductsByYear/{year}", "/api/product/getPopularProductsByYear/{year}")
                .build();

        mockMvc.perform(get("/api/product/getPopularProductsByYear/{year}", 2023)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetProductById_thenReturnProduct() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(product);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/{id}", "/api/product/{id}")
                .build();

        mockMvc.perform(get("/api/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetProductsByType_thenReturnSetProduct() throws Exception {
        Set<Product> list = Set.of(product);
        Mockito.when(service.findByIdType(any())).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/getByType/{id}", "/api/product/getByType/{id}")
                .build();

        mockMvc.perform(get("/api/product/getByType/{id}", "FRUIT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSearchByKeyword_thenReturnProduct() throws Exception {
        Set<ProductResponse> list = Set.of(productResponse);
        Mockito.when(service.searchByKeyword(any())).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/search/{keyword}", "/api/product/search/{keyword}")
                .build();

        mockMvc.perform(get("/api/product/search/{keyword}", "Apple")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    private MockMultipartFile createMultipartFile(String originalContent, String requetsPart, String filename, String contentType){
        return new MockMultipartFile(requetsPart, filename, contentType, originalContent.getBytes());
    }

    @Test
    public void createProduct() throws Exception {

//        TypeProduct type1 = new TypeProduct("FRUITS", "Fruits");
//        TypeProduct type2 = new TypeProduct("VEGE", "Vegetables");
//        Set<TypeProduct> listType = new HashSet<>();
//        listType.add(type1);
//        listType.add(type2);

//        String fileName = "apple.png";
//        MockMultipartFile sampleFile = new MockMultipartFile(
//                "uploaded-file",
//                fileName,
//                "text/plain",
//                "This is the file content".getBytes());
//        MockMultipartHttpServletRequestBuilder multipartRequest =
//                MockMvcRequestBuilders.multipart("/api/product/create");


//        ObjectMapper mapper = new ObjectMapper();
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson = ow.writeValueAsString(product);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/create", "/api/product/create")
                .build();

//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/create")
//                        .file(sampleFile)
//                        .param("product", String.valueOf(product))
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
////                        .content(requestJson))
//                .andExpect(status().isOk());

        String product = "{ \"name\": \"Chuoi\", \"price\": 123, \"types\": [{\"id\": \"FRUIT\" }]}";
        MockMultipartFile filepart = createMultipartFile(product, "formData", "", "application/json");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/create")
                        .file(filepart).characterEncoding("UTF_8"))
                .andExpect(status().isOk());

    }

//    @Test
//    public void updateProduct() throws Exception {
//
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson = ow.writeValueAsString(type);
//
//        mockMvc = MockMvcBuilders.standaloneSetup(controller)
//                .addPlaceholderValue("/api/type-product/update/{id}", "/api/type-product/update/{id}")
//                .build();
//
//        mockMvc.perform(patch("/api/type-product/update/{id}", "FRUITS")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk());
//    }

    @Test
    public void whenDeleteProduct_thenNothing() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/product/delete/{id}", "/api/product/delete/{id}")
                .build();

        mockMvc.perform(delete("/api/product/delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
