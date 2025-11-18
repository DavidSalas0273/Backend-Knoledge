package com.knoledge.backend.repositories;

import com.knoledge.backend.models.Clase;
import com.knoledge.backend.models.ClaseEnrollment;
import com.knoledge.backend.models.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseEnrollmentRepository extends JpaRepository<ClaseEnrollment, Long> {
    boolean existsByClaseAndStudent(Clase clase, Usuario student);
    Optional<ClaseEnrollment> findByClaseAndStudent(Clase clase, Usuario student);
    List<ClaseEnrollment> findByStudent(Usuario student);
    List<ClaseEnrollment> findByClase(Clase clase);
    void deleteByClaseAndStudent(Clase clase, Usuario student);
}
