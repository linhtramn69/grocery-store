package example.service;

import org.example.enums.Role;
import org.example.exception.ApiRequestException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.constants.ErrorMessage.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository repo;
    @Mock
    private PasswordEncoder passwordEncoder;
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
    public void whenGetAllUsers_thenReturnUsersList() {
        List<User> list = new ArrayList<>();
        list.add(user);

        Mockito.when(repo.findAll()).thenReturn(list);

        List<User> listReturn = service.findAll();

        assertEquals(1, listReturn.size());
        assertNotNull(listReturn);
    }

    @Test
    public void whenGetAllUsers_thenReturnEmptyUsersList() {
        Mockito.when(repo.findAll()).thenReturn(Collections.emptyList());

        List<User> listReturn = service.findAll();

        assertEquals(0, listReturn.size());
    }

    @Test
    public void whenGetUserByUsername_thenReturnUserObject() {
        Mockito.when(repo.findByUsername(any())).thenReturn(Optional.of(user));

        User userReturn = service.findByUsername("linhtram");

        assertNotNull(userReturn);
        assertEquals(userReturn.getId(), user.getId());
        assertEquals(userReturn.getUsername(), user.getUsername());
    }

    @Test
    public void whenGetUserByUsername_thenReturnNotFoundUserException() {
        Mockito.when(repo.findByUsername(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.findByUsername("linhtram");
        });

        assertTrue(thrown.getMessage().contains(USER_NOT_FOUND));
    }

    @Test
    public void whenUpdateUser_thenReturnUserObject(){
        Mockito.when(repo.findByUsername(any())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(any())).thenReturn("123");
        Mockito.when(repo.save(any())).thenReturn(user);

        User userReturn = service.update(user, "linhtram");

        assertEquals("linhtram", userReturn.getUsername());
        assertEquals("123", userReturn.getPassword());
    }

    @Test
    public void whenUpdateUser_thenReturnNotFoundUser() {
        Mockito.when(repo.findByUsername(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.update(user,"linhtram");
        });

        assertTrue(thrown.getMessage().contains(USER_NOT_FOUND));
        verify(passwordEncoder, never()).encode(any());
        verify(repo, never()).save(any(User.class));
    }

    @Test
    public void whenDeleteUser_thenNothing() {
        Mockito.when(repo.findByUsername(any())).thenReturn(Optional.of(user));
        willDoNothing().given(repo).delete(any());

        service.delete("linhtram");

        verify(repo, times(1)).findByUsername(any());
        verify(repo, times(1)).delete(any());
    }

    @Test
    public void whenDeleteUser_thenReturnNotFoundUserException() {
        Mockito.when(repo.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            service.delete("linhtram");
        });

        verify(repo, never()).delete(any());
    }
}
