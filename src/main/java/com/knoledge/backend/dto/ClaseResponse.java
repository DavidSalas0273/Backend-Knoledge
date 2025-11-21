package com.knoledge.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ClaseResponse {

    private Long id;
    private String name;
    private String description;
    private String course;
    private LocalDate startDate;
    private LocalTime classTime;
    private Integer maxStudents;
    private Boolean autoApprove;
    private Boolean lateJoin;
    private Boolean notifications;
    private Boolean publicClass;
    private Integer durationMinutes;
    private String topics;
    private String code;
    private LocalDateTime createdAt;
    private List<ClaseMaterialResponse> materials;
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private String enrollmentStatus;

    public ClaseResponse() {
    }

    public ClaseResponse(Long id, String name, String description, String course, LocalDate startDate,
            LocalTime classTime, Integer maxStudents, Boolean autoApprove, Boolean lateJoin,
            Boolean notifications, Boolean publicClass, Integer durationMinutes, String topics,
            String code, LocalDateTime createdAt, List<ClaseMaterialResponse> materials) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.course = course;
        this.startDate = startDate;
        this.classTime = classTime;
        this.maxStudents = maxStudents;
        this.autoApprove = autoApprove;
        this.lateJoin = lateJoin;
        this.notifications = notifications;
        this.publicClass = publicClass;
        this.durationMinutes = durationMinutes;
        this.topics = topics;
        this.code = code;
        this.createdAt = createdAt;
        this.materials = materials;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getClassTime() {
        return classTime;
    }

    public void setClassTime(LocalTime classTime) {
        this.classTime = classTime;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public Boolean getLateJoin() {
        return lateJoin;
    }

    public void setLateJoin(Boolean lateJoin) {
        this.lateJoin = lateJoin;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public Boolean getPublicClass() {
        return publicClass;
    }

    public void setPublicClass(Boolean publicClass) {
        this.publicClass = publicClass;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ClaseMaterialResponse> getMaterials() {
        return materials;
    }

    public void setMaterials(List<ClaseMaterialResponse> materials) {
        this.materials = materials;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }
}
