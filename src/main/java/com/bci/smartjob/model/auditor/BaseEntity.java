package com.bci.smartjob.model.auditor;

import com.bci.smartjob.util.Constants;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public abstract class BaseEntity {
	@Column(name = "estado")
	private Boolean estado;
	@Column(name = "fecha_creacion")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
	@Column(name = "usuario_creacion")
	private String usuarioCreacion;
	@Column(name = "fecha_actualizacion")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaActualizacion;
	@Column(name = "usuario_actualizacion")
	private String usuarioActualizacion;
	
	@PrePersist
    public void prePersist() {
    	if(this.usuarioCreacion == null) {
	    	UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()) {
				this.usuarioCreacion = Constants.APPLICATION_NAME;
			} else {
				this.usuarioCreacion = authentication.getLoginOut().getId().toString();
			}
		}
    	this.fechaCreacion = new Date();
    	this.estado = true;
    }
    @PreUpdate
    public void PreUpdate() {
    	if(this.usuarioActualizacion == null) {
    		UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()) {
				this.usuarioActualizacion = Constants.APPLICATION_NAME;
			} else {
				this.usuarioActualizacion = authentication.getLoginOut().getId().toString();
			}
    	}    	
    	this.fechaActualizacion = new Date();
    	if(this.estado == null) {
    		this.estado = false;
    	}
    }
}
