package com.knoledge.backend.dto;

import com.knoledge.backend.models.CourseMaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseMaterialRequest {

    @NotNull
    private CourseMaterialType type;

    @NotBlank
    private String title;

    private String description;

    private String resourceUrl;

    public CourseMaterialType getType() {
        return type;
    }

    public void setType(CourseMaterialType type) {
        this.type = type;
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

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
