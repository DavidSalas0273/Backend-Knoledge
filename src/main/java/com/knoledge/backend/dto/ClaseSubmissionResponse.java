package com.knoledge.backend.dto;

import java.time.LocalDateTime;

public class ClaseSubmissionResponse {

    private Long id;
    private Long activityId;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private String comment;
    private String fileUrl;
    private String fileName;
    private String linkUrl;
    private LocalDateTime createdAt;

    public ClaseSubmissionResponse() {
    }

    public ClaseSubmissionResponse(Long id, Long activityId, Long studentId, String studentName,
            String studentEmail, String comment, String fileUrl, String fileName, String linkUrl,
            LocalDateTime createdAt) {
        this.id = id;
        this.activityId = activityId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.comment = comment;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.linkUrl = linkUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
