package com.bci.smartjob.controller;

import com.bci.smartjob.model.Phones;
import com.bci.smartjob.model.User;
import com.bci.smartjob.service.UserService;
import com.bci.smartjob.util.Constants;
import io.swagger.annotations.ApiOperation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "Create a new user")
    public ResponseEntity<Object> createUser(@Validated @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Aquí se manejarán los errores de validación
            Map<String, String> errorResponse = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorResponse.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        // Lógica de validación y creación de usuario en el servicio
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getCause();
            if (rootCause instanceof ConstraintViolationException) {
                SQLException sqlException = ((ConstraintViolationException) rootCause).getSQLException();
                if (sqlException.getSQLState().equals(Constants.ERROR_CONSTRAINT)) {
                    // Excepción específica de violación de la restricción de integridad
                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("mensaje", "El correo ya está registrado");
                    return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                }
            }
            // Otros casos de excepción de la base de datos
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("mensaje", "Error al procesar la solicitud: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Otros casos de excepciones generales
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("mensaje", "Error al procesar la solicitud: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/{userId}")
    @ApiOperation(value = "Get user by ID")
    public ResponseEntity<Object> getUserById(@PathVariable UUID userId) {
        // Lógica para obtener un usuario por su ID en el servicio
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("mensaje", "Error al procesar la solicitud: " + "Usuario no encontrado " +"id = " + userId);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }
    // Endpoint para actualizar un usuario
    @PutMapping("/update/{userId}")
    @ApiOperation(value = "Update user by ID")
    public ResponseEntity<Object> updateUser(@PathVariable UUID userId, @RequestBody User updatedUser) {
        User user = userService.getUserById(userId);
        if (user != null) {
            // Actualizar detalles del usuario
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());

            // Actualizar números de teléfono
            List<Phones> updatedPhones = updatedUser.getPhones();
            if (updatedPhones != null && !updatedPhones.isEmpty()) {
                List<Phones> userPhones = user.getPhones();
                userPhones.clear(); // Eliminar los números de teléfono actuales
                userPhones.addAll(updatedPhones); // Agregar los nuevos números de teléfono
            }

            user.setLastLogin(LocalDateTime.now());

            User savedUser = userService.updateUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            // Manejo de usuario no encontrado
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("mensaje", "Usuario no encontrado con ID: " + userId);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para eliminar un usuario
    @DeleteMapping("/delete/{userId}")
    @ApiOperation(value = "Delete user by ID")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
        boolean isDeleted = userService.deleteUser(userId);
        Map<String, String> responseBody = new HashMap<>();
        if (isDeleted) {
            responseBody.put("mensaje","Usuario eliminado con éxito ID: " + userId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            // Manejo de usuario no encontrado o error al eliminar
            responseBody.put("mensaje", "Error al eliminar usuario con ID: " + userId + " , No Existe");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }

}