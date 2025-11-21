package com.knoledge.backend.dto;

import com.knoledge.backend.models.Assignment;
import java.time.LocalDateTime;
import java.util.List;

public class AssignmentResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private List<AssignmentSubmissionResponse> submissions;

    public static AssignmentResponse fromEntity(Assignment assignment, List<AssignmentSubmissionResponse> submissions) {
        AssignmentResponse response = new AssignmentResponse();
        response.id = assignment.getId();
        response.title = assignment.getTitle();
        response.description = assignment.getDescription();
        response.dueDate = assignment.getDueDate();
        response.submissions = submissions;
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public List<AssignmentSubmissionResponse> getSubmissions() {
        return submissions;
    }
}
