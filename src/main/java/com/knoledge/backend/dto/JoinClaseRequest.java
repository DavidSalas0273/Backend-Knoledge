package com.knoledge.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class JoinClaseRequest {

    @NotBlank(message = "El código es obligatorio")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
