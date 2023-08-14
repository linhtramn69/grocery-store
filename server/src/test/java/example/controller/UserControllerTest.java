package example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.controller.UserController;
import org.example.enums.Role;
import org.example.model.Orders;
import org.example.model.User;
import org.example.service.UserService;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController controller;
    @Mock
    private UserService service;
    @Autowired
    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setFullName("Nguyen Linh Tram");
        user.setEmail("linhtram@gmail.com");
        user.setPhone("0797381985");
        user.setUsername("linhtram");
        user.setPassword("123");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
    }

    @Test
    public void whenGetAllUsers_thenReturnListUser() throws Exception {
        List<User> list = List.of(user);

        Mockito.when(service.findAll()).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/user/getAll", "/api/user/getAll")
                .build();

        mockMvc.perform(get("/api/user/getAll")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetUserByUsername_thenReturnUser() throws Exception {
        Mockito.when(service.findByUsername("linhtram")).thenReturn(user);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/user/{username}", "/api/user/{username}")
                .build();

        mockMvc.perform(get("/api/user/{username}", "linhtram")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateUser_thenReturnUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/user/update/{username}", "/api/user/update/{username}")
                .build();

        mockMvc.perform(patch("/api/user/update/{username}", "linhtram")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteUser_thenNothing() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("/api/user/delete/{username}", "/api/user/delete/{username}")
                .build();

        mockMvc.perform(delete("/api/user/delete/{username}", "linhtram")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
