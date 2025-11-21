package com.knoledge.backend.dto;

import com.knoledge.backend.models.AssignmentSubmission;
import java.time.LocalDateTime;

public class AssignmentSubmissionResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String fileUrl;
    private Double grade;
    private String feedback;
    private LocalDateTime submittedAt;

    public static AssignmentSubmissionResponse fromEntity(AssignmentSubmission submission, String downloadUrl) {
        AssignmentSubmissionResponse response = new AssignmentSubmissionResponse();
        response.id = submission.getId();
        response.studentId = submission.getStudent().getId();
        response.studentName = submission.getStudent().getName();
        response.fileUrl = downloadUrl;
        response.grade = submission.getGrade();
        response.feedback = submission.getFeedback();
        response.submittedAt = submission.getSubmittedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Double getGrade() {
        return grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}
