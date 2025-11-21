package com.knoledge.backend.dto;

import com.knoledge.backend.models.CourseEnrollment;
import com.knoledge.backend.models.EnrollmentStatus;

public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private EnrollmentStatus status;

    public static EnrollmentResponse fromEntity(CourseEnrollment enrollment) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.id = enrollment.getId();
        response.studentId = enrollment.getStudent().getId();
        response.studentName = enrollment.getStudent().getName();
        response.studentEmail = enrollment.getStudent().getEmail();
        response.status = enrollment.getStatus();
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

    public String getStudentEmail() {
        return studentEmail;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }
}
