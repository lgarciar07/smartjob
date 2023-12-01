package com.bci.smartjob.repository;

import com.bci.smartjob.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método personalizado para obtener un usuario por su ID
    Optional<User> findById(UUID userId);

}
