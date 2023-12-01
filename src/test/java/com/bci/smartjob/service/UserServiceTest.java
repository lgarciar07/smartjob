package com.bci.smartjob.service;

import com.bci.smartjob.model.User;
import com.bci.smartjob.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepositoryMock);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        when(userRepositoryMock.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(userId, createdUser.getId());
        assertTrue(createdUser.isActive());
        assertNotNull(createdUser.getToken());
        assertNotNull(createdUser.getCreated());
        assertNotNull(createdUser.getModified());
        assertNotNull(createdUser.getLastLogin());

        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());

        verify(userRepositoryMock, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        when(userRepositoryMock.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        assertNotNull(updatedUser.getModified());

        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);
        verify(userRepositoryMock, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        boolean isDeleted = userService.deleteUser(userId);

        assertFalse(isDeleted);
        verify(userRepositoryMock, never()).delete(any());
    }
}

