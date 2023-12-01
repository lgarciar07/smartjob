package com.bci.smartjob.service;

import com.bci.smartjob.model.User;
import com.bci.smartjob.repository.UserRepository;
import com.bci.smartjob.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken(Util.generateUuid());
        user.setActive(true);
        return userRepository.save(user);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // Actualizar un usuario
    public User updateUser(User user) {
        // Actualizar otros campos si es necesario
        user.setModified(LocalDateTime.now());
        return userRepository.save(user);
    }

    // Eliminar un usuario por su ID
    public boolean deleteUser(UUID userId) {
        User user = getUserById(userId);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

}
