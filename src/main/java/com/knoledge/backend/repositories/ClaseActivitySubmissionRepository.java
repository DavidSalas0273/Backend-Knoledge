package com.knoledge.backend.repositories;

import com.knoledge.backend.models.ClaseActivity;
import com.knoledge.backend.models.ClaseActivitySubmission;
import com.knoledge.backend.models.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseActivitySubmissionRepository extends JpaRepository<ClaseActivitySubmission, Long> {
    List<ClaseActivitySubmission> findByActivityOrderByCreatedAtDesc(ClaseActivity activity);
    Optional<ClaseActivitySubmission> findFirstByActivityAndStudentOrderByCreatedAtDesc(ClaseActivity activity, Usuario student);
    List<ClaseActivitySubmission> findByActivityAndStudent(ClaseActivity activity, Usuario student);
    long countByActivity(ClaseActivity activity);
}
