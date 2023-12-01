package com.bci.smartjob.security;

import com.bci.smartjob.bean.in.LoginIn;
import com.bci.smartjob.model.auditor.UserAuthentication;
import com.bci.smartjob.security.service.JWTService;
import com.bci.smartjob.security.service.impl.JWTServiceImpl;
import com.bci.smartjob.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTService jwtService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		UserAuthentication authentication = new UserAuthentication(Constants.USERNAME, Constants.PASSWORD);
		return authenticationManager.authenticate(authentication);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		log.info("Authentication success. " + authResult.getName());

		Map<String, Object> result = jwtService.create(authResult);
		String token = String.valueOf(result.get(Constants.KEY_TOKEN));

		response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);

		Map<String, Object> body = new HashMap<>();
		body.put(Constants.KEY_TOKEN, token);
		body.put(Constants.KEY_MESSAGE, String.format("Hola %s, has iniciado sesion!", ((User) authResult.getPrincipal()).getUsername()));

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType(Constants.APPLICATION_JSON);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		log.info("Authentication fail. {}", failed.getMessage());
		Map<String, Object> body = new HashMap<>();
		body.put(Constants.KEY_MESSAGE, Constants.Message.ERROR_INCORRECT_USERNAME_PASS);
		body.put(Constants.KEY_ERROR, failed.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType(Constants.APPLICATION_JSON);
	}
}
