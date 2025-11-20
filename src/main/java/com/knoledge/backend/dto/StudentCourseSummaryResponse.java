package com.knoledge.backend.dto;

import com.knoledge.backend.models.CourseEnrollment;
import com.knoledge.backend.models.EnrollmentStatus;

public class StudentCourseSummaryResponse {

    private Long id;
    private String title;
    private String category;
    private String description;
    private String teacherName;
    private Integer durationMinutes;
    private EnrollmentStatus status;

    public static StudentCourseSummaryResponse fromEnrollment(CourseEnrollment enrollment) {
        StudentCourseSummaryResponse response = new StudentCourseSummaryResponse();
        response.id = enrollment.getCourse().getId();
        response.title = enrollment.getCourse().getTitle();
        response.category = enrollment.getCourse().getCategory();
        response.description = enrollment.getCourse().getDescription();
        response.teacherName = enrollment.getCourse().getTeacher() != null ? enrollment.getCourse().getTeacher().getName() : null;
        response.durationMinutes = enrollment.getCourse().getDurationMinutes();
        response.status = enrollment.getStatus();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }
}
