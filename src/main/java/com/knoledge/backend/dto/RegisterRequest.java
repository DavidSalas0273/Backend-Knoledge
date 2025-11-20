package com.knoledge.backend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.knoledge.backend.models.Role;
import com.knoledge.backend.validation.InputSanitizer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @Size(min = 2, max = 120)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÜüÑñ]+(?: [A-Za-zÁÉÍÓÚáéíóúÜüÑñ]+)*$", message = "El nombre solo puede contener letras y un espacio entre palabras")
    private String name;

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]{8,64}$",
            message = "La contraseña debe tener al menos una letra, un número y entre 8 y 64 caracteres sin espacios")
    private String password;

    @NotNull
    private Role role = Role.STUDENT;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = InputSanitizer.sanitizeName(name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = InputSanitizer.sanitizeEmail(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = InputSanitizer.sanitizePassword(password);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonSetter("role")
    public void setRole(String role) {
        if (role == null || role.isBlank()) {
            this.role = Role.STUDENT;
            return;
        }
        try {
            this.role = Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            this.role = Role.STUDENT;
        }
    }
}
