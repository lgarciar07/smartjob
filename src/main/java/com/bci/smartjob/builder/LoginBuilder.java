package com.bci.smartjob.builder;

import com.bci.smartjob.bean.out.LoginOut;
import com.bci.smartjob.model.Login;
import com.bci.smartjob.util.Util;

public class LoginBuilder {
		public static LoginOut findByUsuario(Login user) {
		return Util.objectToObject(LoginOut.class, user);
	}
}