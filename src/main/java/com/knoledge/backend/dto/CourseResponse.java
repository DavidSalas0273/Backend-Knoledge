package com.knoledge.backend.dto;

import com.knoledge.backend.models.Course;
import java.time.LocalDateTime;
import java.util.List;

public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private List<String> topics;
    private Integer durationMinutes;
    private String joinCode;
    private String teacherName;
    private String teacherEmail;
    private LocalDateTime createdAt;

    public static CourseResponse fromEntity(Course course) {
        CourseResponse response = new CourseResponse();
        response.id = course.getId();
        response.title = course.getTitle();
        response.description = course.getDescription();
        response.category = course.getCategory();
        response.topics = course.getTopics();
        response.durationMinutes = course.getDurationMinutes();
        response.joinCode = course.getJoinCode();
        response.teacherName = course.getTeacher() != null ? course.getTeacher().getName() : null;
        response.teacherEmail = course.getTeacher() != null ? course.getTeacher().getEmail() : null;
        response.createdAt = course.getCreatedAt();
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

    public List<String> getTopics() {
        return topics;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
