package com.knoledge.backend.repositories;

import com.knoledge.backend.models.Assignment;
import com.knoledge.backend.models.AssignmentSubmission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByAssignment(Assignment assignment);
}
