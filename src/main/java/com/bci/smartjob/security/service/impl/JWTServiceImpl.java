package com.bci.smartjob.security.service.impl;

import com.bci.smartjob.bean.out.LoginOut;
import com.bci.smartjob.security.service.JWTService;
import com.bci.smartjob.security.service.SimpleGrantedAuthorityMixin;
import com.bci.smartjob.service.LoginService;
import com.bci.smartjob.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.*;

@Component
public class JWTServiceImpl implements JWTService {
	
	public static final String SECRET = Base64Utils.encodeToString("Key.Secret.Token.The.Mk".getBytes());
	public static final long EXPIRATION_DATE = 36000000L;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_STRING = "authorities";
	public static final String USER_STRING = "user";
	
	@Autowired
	private LoginService userService;
	
	@Override
	public Map <String, Object> create(Authentication auth) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String username = ((User) auth.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		
		LoginOut userOut = userService.findByUsuario(username);
		
		if (userOut == null) {
			throw new IOException("Usuario no tiene acceso.");
		}
		
		Claims claims = Jwts.claims();
		claims.put(AUTHORITIES_STRING, new ObjectMapper().writeValueAsString(roles));
		claims.put(USER_STRING, new ObjectMapper().writeValueAsString(userOut));
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
				.compact();
		
		result.put(Constants.KEY_TOKEN, token);
		result.put(USER_STRING, userOut);
		
		return result;
	}
	
	@Override
	public boolean validate(String token) {
		boolean result = true;
		try {
			getClaims(token);
		}catch(JwtException | IllegalArgumentException ex) {
			result = false;
		}
		return result;
	}
	
	@Override
	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(resolve(token)).getBody();
	}
	
	@Override
	public Collection<GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get(AUTHORITIES_STRING);
		return Arrays.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class).readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
	}

	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		} else {
			return null;
		}
	}

	@Override
	public LoginOut getLoginUserOut(String token) throws IOException {
		Object user = getClaims(token).get(USER_STRING);
		return new ObjectMapper().readValue(user.toString(),LoginOut.class);
	}	
}
