package com.knoledge.backend.dto;

import com.knoledge.backend.models.Course;

public class TeacherCourseSummaryResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Integer durationMinutes;
    private String joinCode;
    private long pendingEnrollments;
    private long approvedEnrollments;

    public static TeacherCourseSummaryResponse of(Course course, long pending, long approved) {
        TeacherCourseSummaryResponse response = new TeacherCourseSummaryResponse();
        response.id = course.getId();
        response.title = course.getTitle();
        response.description = course.getDescription();
        response.category = course.getCategory();
        response.durationMinutes = course.getDurationMinutes();
        response.joinCode = course.getJoinCode();
        response.pendingEnrollments = pending;
        response.approvedEnrollments = approved;
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

    public String getCategory() {
        return category;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public long getPendingEnrollments() {
        return pendingEnrollments;
    }

    public long getApprovedEnrollments() {
        return approvedEnrollments;
    }
}
