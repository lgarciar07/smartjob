package com.bci.smartjob.security.service;

import com.bci.smartjob.bean.out.LoginOut;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface JWTService {
	public Map <String, Object> create(Authentication auth) throws IOException;
	public boolean validate(String token);
	public Claims getClaims(String token);
	public String getUsername(String token);
	public Collection<GrantedAuthority> getRoles(String token) throws IOException;
	public String resolve(String token);
	public LoginOut getLoginUserOut(String token) throws IOException;
}
