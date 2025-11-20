package com.knoledge.backend.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clases")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String course;

    private LocalDate startDate;

    private LocalTime classTime;

    @Column(nullable = false)
    private Integer maxStudents = 30;

    private boolean autoApprove = true;
    private boolean lateJoin = true;
    private boolean notifications = true;
    private boolean publicClass = false;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    private Integer durationMinutes;

    @Column(length = 1000)
    private String topics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Usuario teacher;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaseMaterial> materials = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
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

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public boolean isLateJoin() {
        return lateJoin;
    }

    public void setLateJoin(boolean lateJoin) {
        this.lateJoin = lateJoin;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isPublicClass() {
        return publicClass;
    }

    public void setPublicClass(boolean publicClass) {
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

    public Usuario getTeacher() {
        return teacher;
    }

    public void setTeacher(Usuario teacher) {
        this.teacher = teacher;
    }

    public List<ClaseMaterial> getMaterials() {
        return materials;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addMaterial(ClaseMaterial material) {
        materials.add(material);
        material.setClase(this);
    }
}
