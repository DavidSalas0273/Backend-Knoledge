package com.knoledge.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String topicsSerialized;

    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Usuario teacher;

    @Column(nullable = false, unique = true, length = 12)
    private String joinCode;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Usuario getTeacher() {
        return teacher;
    }

    public void setTeacher(Usuario teacher) {
        this.teacher = teacher;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    public void onCreate() {
        if (joinCode == null) {
            joinCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
        }
        createdAt = LocalDateTime.now();
    }

    public List<String> getTopics() {
        if (topicsSerialized == null || topicsSerialized.isBlank()) {
            return new ArrayList<>();
        }
        String[] tokens = topicsSerialized.split(",");
        List<String> topics = new ArrayList<>();
        for (String token : tokens) {
            String value = token.trim();
            if (!value.isEmpty()) {
                topics.add(value);
            }
        }
        return topics;
    }

    public void setTopics(List<String> topics) {
        if (topics == null || topics.isEmpty()) {
            this.topicsSerialized = null;
            return;
        }
        this.topicsSerialized = String.join(",", topics);
    }
}
