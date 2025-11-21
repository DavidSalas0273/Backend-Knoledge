package com.knoledge.backend.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class LocalCorsFilter implements Filter {

    private final List<String> allowedOrigins;

    public LocalCorsFilter(@Value("${cors.allowed-origins:http://localhost:5173,https://frontend-knoledge.vercel.app}") String origins) {
        List<String> parsed = new ArrayList<>();
        if (origins != null && !origins.isBlank()) {
            Arrays.stream(origins.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(parsed::add);
        }
        if (!parsed.contains("http://localhost:5173")) {
            parsed.add("http://localhost:5173");
        }
        this.allowedOrigins = parsed;
    }

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        boolean originAllowed = origin != null && allowedOrigins.contains(origin);
        if (originAllowed) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", "Origin");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        response.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");

        response.setHeader("Access-Control-Allow-Headers",
                "Authorization, Content-Type, Accept, Origin, X-Requested-With");

        response.setHeader("Access-Control-Max-Age", "3600");

        // Responder preflight y cortar
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
