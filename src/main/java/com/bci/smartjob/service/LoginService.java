package com.bci.smartjob.service;

import com.bci.smartjob.bean.out.LoginOut;
import com.bci.smartjob.model.Login;

public interface LoginService {
	
	LoginOut findByUsuario(String usuario);
}
