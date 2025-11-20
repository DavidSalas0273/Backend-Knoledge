package com.knoledge.backend.dto;

import com.knoledge.backend.models.CourseMaterial;
import com.knoledge.backend.models.CourseMaterialType;
import java.time.LocalDateTime;

public class CourseMaterialResponse {

    private Long id;
    private String title;
    private String description;
    private CourseMaterialType type;
    private String resourceUrl;
    private String downloadUrl;
    private LocalDateTime createdAt;

    public static CourseMaterialResponse fromEntity(CourseMaterial material, String downloadUrl) {
        CourseMaterialResponse response = new CourseMaterialResponse();
        response.id = material.getId();
        response.title = material.getTitle();
        response.description = material.getDescription();
        response.type = material.getType();
        response.resourceUrl = material.getResourceUrl();
        response.downloadUrl = downloadUrl != null ? "/files/" + downloadUrl : null;
        response.createdAt = material.getCreatedAt();
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

    public CourseMaterialType getType() {
        return type;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
