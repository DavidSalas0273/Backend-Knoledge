package com.knoledge.backend.services;

import com.knoledge.backend.dto.AuthRequest;
import com.knoledge.backend.dto.AuthResponse;
import com.knoledge.backend.dto.RegisterRequest;
import com.knoledge.backend.dto.UserDto;
import com.knoledge.backend.models.Role;
import com.knoledge.backend.models.Usuario;
import com.knoledge.backend.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Credenciales ya utilizadas. Prueba con otro correo electrónico.");
        }
        Role role = request.getRole() == null ? Role.STUDENT : request.getRole();
        Usuario user = new Usuario(request.getName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), role);
        Usuario saved = usuarioRepository.save(user);
        return buildResponse(saved, "Registrado correctamente");
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return buildResponse(user, "Inicio de sesión exitoso");
    }

    private AuthResponse buildResponse(Usuario user, String message) {
        String token = jwtService.generateToken(user);
        UserDto dto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
        return new AuthResponse(token, null, dto, message);
    }
}
