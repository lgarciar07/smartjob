package com.bci.smartjob.security.service.impl;

import com.bci.smartjob.bean.out.LoginOut;
import com.bci.smartjob.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginUserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private LoginService loginService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginOut userOut = loginService.findByUsuario("admin");
		if (userOut == null) {
			throw new UsernameNotFoundException("Usuario " + username + " no se encuentra registrado.");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ADM"));
		if (authorities.isEmpty()) {
			throw new UsernameNotFoundException("FUsuario '" + username + "' sin roles");
		}
		return new User(userOut.getUsuario(), userOut.getPassword(), userOut.getEstado(), true, true, true, authorities);
	}
}
