package com.knoledge.backend.dto;

import java.time.LocalDateTime;

public class ClaseActivityResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String youtubeUrl;
    private LocalDateTime createdAt;
    private long submissionCount;

    public ClaseActivityResponse() {
    }

    public ClaseActivityResponse(Long id, String title, String description, LocalDateTime dueDate,
            String youtubeUrl, LocalDateTime createdAt, long submissionCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.youtubeUrl = youtubeUrl;
        this.createdAt = createdAt;
        this.submissionCount = submissionCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(long submissionCount) {
        this.submissionCount = submissionCount;
    }
}
