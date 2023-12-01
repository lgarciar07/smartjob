package com.bci.smartjob.model.auditor;

import com.bci.smartjob.bean.out.LoginOut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private LoginOut loginOut;

	public UserAuthentication(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public UserAuthentication(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities, LoginOut userOut) {
		super(principal, credentials, authorities);
		this.loginOut = userOut;
	}

	public LoginOut getLoginOut() {
		return loginOut;
	}

	public void setLoginOut(LoginOut loginOut) {
		this.loginOut = loginOut;
	}
}
