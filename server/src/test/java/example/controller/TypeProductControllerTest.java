package example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.controller.TypeProductController;
import org.example.model.TypeProduct;
import org.example.service.TypeProductService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TypeProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private TypeProductController controller;
    @Mock
    private TypeProductService service;

    private TypeProduct type;

    @BeforeEach
    public void init() {
        type = new TypeProduct();
        type.setId("FRUITS");
        type.setName("Fruits");
    }

    @Test
    public void whenGetAllType_thenReturnListType() throws Exception {
        List<TypeProduct> list = List.of(type);

        Mockito.when(service.findAll()).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/type-product/getAll", "/api/type-product/getAll")
                .build();

        mockMvc.perform(get("/api/type-product/getAll")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem("FRUITS")))
                .andExpect(jsonPath("$[*].name", hasItem("Fruits")));
    }

    @Test
    public void whenGetTypeById_thenReturnType() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/type-product/{id}", "/api/type-product/{id}")
                .build();

        mockMvc.perform(get("/api/type-product/{id}", "FRUITS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateType_thenReturnNewType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(type);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/type-product/create", "/api/type-product/create")
                .build();

        mockMvc.perform(post("/api/type-product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateType_thenReturnType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(type);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/type-product/update/{id}", "/api/type-product/update/{id}")
                .build();

        mockMvc.perform(patch("/api/type-product/update/{id}", "FRUITS")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteType_thenNothing() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/type-product/delete/{id}", "/api/type-product/delete/{id}")
                .build();

        mockMvc.perform(delete("/api/type-product/delete/{id}", "FRUITS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
