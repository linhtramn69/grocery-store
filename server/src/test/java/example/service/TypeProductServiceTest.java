package example.service;

import org.example.exception.ApiRequestException;
import org.example.model.TypeProduct;
import org.example.repository.TypeProductRepository;
import org.example.service.impl.TypeProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.constants.ErrorMessage.TYPE_PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeProductServiceTest {
    @InjectMocks
    private TypeProductServiceImpl service;
    @Mock
    private TypeProductRepository repo;
    private TypeProduct type;

    @BeforeEach
    public void init() {
        type = new TypeProduct();
        type.setId("FRUITS");
        type.setName("Fruits");
    }

    @Test
    public void whenGetAllTypes_thenReturnTypesList() {
        List<TypeProduct> list = new ArrayList<>();
        TypeProduct type1 = new TypeProduct("FRUITS", "Fruits");
        TypeProduct type2 = new TypeProduct("VEGE", "Vegetables");
        list.add(type1);
        list.add(type2);

        Mockito.when(repo.findAll()).thenReturn(list);

        List<TypeProduct> listReturn = service.findAll();

        assertEquals(2, listReturn.size());
        assertNotNull(listReturn);
    }

    @Test
    public void whenGetAllTypes_thenReturnEmptyTypesList() {
        Mockito.when(repo.findAll()).thenReturn(Collections.emptyList());

        List<TypeProduct> listReturn = service.findAll();

        assertEquals(0, listReturn.size());
    }

    @Test
    public void whenGetTypeById_thenReturnTypeObject() {
        TypeProduct typeActually = new TypeProduct("FRUITS", "Fruits");

        Mockito.when(repo.findById("FRUITS")).thenReturn(Optional.of(typeActually));

        TypeProduct typeReturn = service.findById("FRUITS");

        assertNotNull(typeReturn);
        assertEquals(typeReturn.getId(), typeActually.getId());
        assertEquals(typeReturn.getName(), typeActually.getName());
    }

    @Test
    public void whenGetTypeById_thenReturnNotFoundTypeException() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            service.findById("FRUITS");
        });

        assertTrue(thrown.getMessage().contains(TYPE_PRODUCT_NOT_FOUND));
    }

    @Test
    public void whenCreateType_thenReturnTypeObject() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());
        Mockito.when(repo.save(type)).thenReturn(type);

        TypeProduct typeReturn = service.create(type);

        assertNotNull(typeReturn);
        verify(repo, times(1)).findById("FRUITS");
        verify(repo, times(1)).save(type);
    }

    @Test
    public void whenCreateType_thenReturnTypeInUseException() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(type));

        assertThrows(ApiRequestException.class, () -> {
            service.create(type);
        });

        verify(repo, never()).save(any(TypeProduct.class));
    }

    @Test
    public void whenUpdateType_thenReturnTypeObject() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.of(type));
        Mockito.when(repo.save(type)).thenReturn(type);

        TypeProduct typeReturn = service.update(type, "FRUITS");

        assertEquals("FRUITS", typeReturn.getId());
        assertEquals("Fruits", typeReturn.getName());

        verify(repo, times(1)).findById("FRUITS");
        verify(repo, times(1)).save(type);
    }

    @Test
    public void whenUpdateType_thenReturnNotFoundTypeException() {
        Mockito.when(repo.findById(any())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            service.update(type, "FRUITS");
        });

        verify(repo, never()).save(any(TypeProduct.class));
    }

    @Test
    public void whenDeleteType_thenNothing() {
        Mockito.lenient().when(repo.findById(any())).thenReturn(Optional.ofNullable(type));
        willDoNothing().given(repo).deleteById(any());

        service.delete("FRUITS");

        verify(repo, times(1)).findById(any());
        verify(repo, times(1)).deleteById(any());
    }

    @Test
    public void whenDeleteType_thenReturnNotFoundTypeException() {
        Mockito.lenient().when(repo.findById(any())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> {
            service.delete("FRUITS");
        });

        verify(repo, never()).deleteById(any());
    }
}
