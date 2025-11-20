package com.knoledge.backend.dto;

import com.knoledge.backend.validation.InputSanitizer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

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
}
