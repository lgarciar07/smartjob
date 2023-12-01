package com.bci.smartjob.model;

import com.bci.smartjob.model.auditor.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@Table(name = "login")
public class Login extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Basic(optional = false)
	@Column(name = "usuario")
	private String usuario;
	@Column(name = "password")
	private String password;

}
