package com.knoledge.backend.dto;

public class AuthResponse {

    private String token;
    private String refreshToken;
    private UserDto user;
    private String message;

    public AuthResponse(String token, String refreshToken, UserDto user, String message) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserDto getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
