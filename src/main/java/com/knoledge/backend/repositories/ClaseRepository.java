package com.knoledge.backend.repositories;

import com.knoledge.backend.models.Clase;
import com.knoledge.backend.models.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
    List<Clase> findByTeacher(Usuario teacher);
    boolean existsByCode(String code);
    Optional<Clase> findByCode(String code);
}
