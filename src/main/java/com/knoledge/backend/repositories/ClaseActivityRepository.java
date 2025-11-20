package com.knoledge.backend.repositories;

import com.knoledge.backend.models.Clase;
import com.knoledge.backend.models.ClaseActivity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseActivityRepository extends JpaRepository<ClaseActivity, Long> {
    List<ClaseActivity> findByClaseOrderByCreatedAtDesc(Clase clase);
}
