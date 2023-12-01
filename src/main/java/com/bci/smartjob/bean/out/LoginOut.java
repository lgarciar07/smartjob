package com.bci.smartjob.bean.out;

import lombok.Data;

@Data
public class LoginOut {
	private Long id;
	private String usuario;
	private String password;
	private Boolean estado;
}
