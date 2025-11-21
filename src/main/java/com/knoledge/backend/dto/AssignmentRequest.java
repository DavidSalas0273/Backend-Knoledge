package com.knoledge.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AssignmentRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String dueDate;

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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
