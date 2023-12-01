package com.bci.smartjob.service.impl;

import com.bci.smartjob.bean.out.LoginOut;
import com.bci.smartjob.builder.LoginBuilder;
import com.bci.smartjob.repository.LoginRepository;
import com.bci.smartjob.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginRepository loginRepository;

	@Override
	public LoginOut findByUsuario(String username) {
		return LoginBuilder.findByUsuario(loginRepository.findByUsuarioAndEstadoTrue(username));
	}	
}
