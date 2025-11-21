package com.knoledge.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String clientOrigin;
    private final String corsOrigins;

    public WebConfig(
            @Value("${client.origin:http://localhost:5173}") String clientOrigin,
            @Value("${cors.allowed-origins:http://localhost:5173,http://localhost:3000}") String corsOrigins) {
        this.clientOrigin = clientOrigin;
        this.corsOrigins = corsOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Parse CORS_ORIGINS if available
        String[] origins = corsOrigins.split(",");
        
        registry.addMapping("/**")
                .allowedOrigins(origins)  // Use actual origins from config
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
