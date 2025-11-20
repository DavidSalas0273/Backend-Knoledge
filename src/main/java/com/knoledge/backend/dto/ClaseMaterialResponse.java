package com.knoledge.backend.dto;

import java.time.LocalDateTime;

public class ClaseMaterialResponse {

    private Long id;
    private String originalName;
    private String contentType;
    private String url;
    private boolean external;
    private LocalDateTime uploadedAt;

    public ClaseMaterialResponse() {
    }

    public ClaseMaterialResponse(Long id, String originalName, String contentType, String url,
            boolean external, LocalDateTime uploadedAt) {
        this.id = id;
        this.originalName = originalName;
        this.contentType = contentType;
        this.url = url;
        this.external = external;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
