package com.knoledge.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClaseRequest {
    private Long teacherId;
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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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
}
