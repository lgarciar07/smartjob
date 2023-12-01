package com.bci.smartjob.repository;

import com.bci.smartjob.model.Login;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends CrudRepository<Login, Long> {

	Login findByUsuarioAndEstadoTrue(String username);
}
