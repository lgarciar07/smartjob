package com.bci.smartjob.controller;

import com.bci.smartjob.model.User;
import com.bci.smartjob.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userServiceTest;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(userServiceTest.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<Object> response = userController.createUser(user, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUser_WithValidationErrors_ReturnsBadRequest() {
        User userWithErrors = new User();
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("user", "fieldName", "Error message 1"));
        fieldErrors.add(new FieldError("user", "anotherField", "Error message 2"));

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Object> response = userController.createUser(userWithErrors, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verificar que el cuerpo de la respuesta contenga los mensajes de error esperados
        Map<String, String> expectedErrorMap = new HashMap<>();
        expectedErrorMap.put("fieldName", "Error message 1");
        expectedErrorMap.put("anotherField", "Error message 2");

        assertEquals(expectedErrorMap, response.getBody());
    }

    @Test
    void testGetUserById_UserFound() {
        UUID userId = UUID.randomUUID(); // ID válido para la prueba
        User user = new User(); // Aquí crea un usuario simulado
        when(userServiceTest.getUserById(userId)).thenReturn(user);

        ResponseEntity<Object> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserById_UserNotFound() {
        UUID userId = UUID.randomUUID(); // ID simulado que no existe en la base de datos
        when(userServiceTest.getUserById(userId)).thenReturn(null);

        ResponseEntity<Object> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        User updatedUser = new User();

        when(userServiceTest.getUserById(userId)).thenReturn(user);
        when(userServiceTest.updateUser(any(User.class))).thenReturn(updatedUser);

        ResponseEntity<Object> response = userController.updateUser(userId, updatedUser);

        // Verifica que se haya llamado al método correspondiente del servicio para obtener y actualizar al usuario
        verify(userServiceTest, times(1)).getUserById(userId);
        verify(userServiceTest, times(1)).updateUser(any(User.class));

        // Verifica el código de estado de la respuesta
        assert response.getStatusCode() == HttpStatus.OK;

        // Verifica que la respuesta contiene el usuario actualizado
        assert response.getBody() != null;
        assert response.getBody() instanceof User;

    }

    @Test
    void testDeleteUser_Success() {
        UUID userId = UUID.randomUUID();

        when(userServiceTest.deleteUser(userId)).thenReturn(true);

        ResponseEntity<Object> response = userController.deleteUser(userId);

        // Verifica que se haya llamado al método correspondiente del servicio para eliminar al usuario
        verify(userServiceTest, times(1)).deleteUser(userId);

        // Verifica el código de estado de la respuesta
        assert response.getStatusCode() == HttpStatus.OK;

        // Verifica que la respuesta contenga el mensaje de éxito
        assert response.getBody() != null;
        assert response.getBody() instanceof Map;
        assert ((Map<?, ?>) response.getBody()).containsKey("mensaje");
        assert ((Map<?, ?>) response.getBody()).get("mensaje").equals("Usuario eliminado con éxito ID: " + userId);
    }

}
