package com.bci.smartjob.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "phones")
public class Phones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "^[1-9]\\d{0,8}$", message = "El número debe contener entre 1 y 9 dígitos y no puede comenzar con 0")
    private String number;

    @NotBlank(message = "El cityCode es obligatorio")
    @Pattern(regexp = "^[0-9]{1,4}$", message = "El cityCode debe ser numérico y tener máximo 4 dígitos")
    private String cityCode;

    @NotBlank(message = "El countryCode es obligatorio")
    @Pattern(regexp = "^[0-9]{1,4}$", message = "El countryCode debe ser numérico y tener máximo 4 dígitos")
    private String countryCode;

}
