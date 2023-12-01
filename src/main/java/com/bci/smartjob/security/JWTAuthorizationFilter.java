package com.bci.smartjob.security;

import com.bci.smartjob.model.auditor.UserAuthentication;
import com.bci.smartjob.security.service.JWTService;
import com.bci.smartjob.security.service.impl.JWTServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTService jwtService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(JWTServiceImpl.HEADER_STRING);

		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}
		UserAuthentication authentication = null;
		if (jwtService.validate(header)) {
			authentication = new UserAuthentication(jwtService.getUsername(header), null, jwtService.getRoles(header), jwtService.getLoginUserOut(header));
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	protected boolean requiresAuthentication(String header) {
		boolean respuesta = true;
		if (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)) {
			respuesta = false;
		}
		return respuesta;
	}
}
