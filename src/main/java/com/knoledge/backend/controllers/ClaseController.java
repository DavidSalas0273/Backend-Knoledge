package com.knoledge.backend.controllers;

import com.knoledge.backend.dto.ClaseActivityRequest;
import com.knoledge.backend.dto.ClaseActivityResponse;
import com.knoledge.backend.dto.ClaseEnrollmentResponse;
import com.knoledge.backend.dto.ClaseMaterialResponse;
import com.knoledge.backend.dto.ClaseRequest;
import com.knoledge.backend.dto.ClaseResponse;
import com.knoledge.backend.dto.ClaseSubmissionResponse;
import com.knoledge.backend.dto.JoinClaseRequest;
import com.knoledge.backend.models.Usuario;
import com.knoledge.backend.services.ClaseService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/classes")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @PostMapping
    public ClaseResponse createClase(@AuthenticationPrincipal Usuario teacher,
            @Valid @RequestBody ClaseRequest request) {
        return claseService.createClase(teacher, request);
    }

    @GetMapping
    public List<ClaseResponse> getClases(@AuthenticationPrincipal Usuario teacher) {
        return claseService.getClasesForTeacher(teacher);
    }

    @GetMapping("/{claseId}")
    public ClaseResponse getClase(@PathVariable Long claseId,
            @AuthenticationPrincipal Usuario requester) {
        return claseService.getClaseDetail(claseId, requester);
    }

    @PutMapping("/{claseId}")
    public ClaseResponse updateClase(@PathVariable Long claseId,
            @AuthenticationPrincipal Usuario teacher,
            @RequestBody ClaseRequest request) {
        return claseService.updateClase(claseId, teacher, request);
    }

    @GetMapping("/student")
    public List<ClaseResponse> getStudentClases(@AuthenticationPrincipal Usuario student) {
        return claseService.getClasesForStudent(student);
    }

    @PostMapping("/join")
    public ClaseResponse joinClase(@AuthenticationPrincipal Usuario student,
            @Valid @RequestBody JoinClaseRequest request) {
        return claseService.joinClase(request.getCode(), student);
    }

    @PostMapping(value = "/{claseId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ClaseMaterialResponse uploadMaterial(@PathVariable Long claseId,
            @AuthenticationPrincipal Usuario teacher,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "linkUrl", required = false) String linkUrl,
            @RequestParam(value = "linkTitle", required = false) String linkTitle) {
        return claseService.addMaterial(claseId, teacher, file, linkUrl, linkTitle);
    }

    @GetMapping("/{claseId}/activities")
    public List<ClaseActivityResponse> getActivities(@PathVariable Long claseId,
            @AuthenticationPrincipal Usuario requester) {
        return claseService.getActivities(claseId, requester);
    }

    @GetMapping("/{claseId}/enrollments")
    public List<ClaseEnrollmentResponse> getEnrollments(@PathVariable Long claseId,
            @AuthenticationPrincipal Usuario teacher) {
        return claseService.getEnrollments(claseId, teacher);
    }

    @DeleteMapping("/{claseId}/leave")
    public void leaveClase(@PathVariable Long claseId, @AuthenticationPrincipal Usuario student) {
        claseService.leaveClase(claseId, student);
    }

    @PostMapping("/{claseId}/activities")
    public ClaseActivityResponse createActivity(@PathVariable Long claseId,
            @AuthenticationPrincipal Usuario teacher,
            @Valid @RequestBody ClaseActivityRequest request) {
        return claseService.createActivity(claseId, teacher, request);
    }

    @PostMapping(value = "/{claseId}/activities/{activityId}/submissions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ClaseSubmissionResponse submitActivity(@PathVariable Long claseId,
            @PathVariable Long activityId,
            @AuthenticationPrincipal Usuario student,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "linkUrl", required = false) String linkUrl) {
        return claseService.submitActivity(claseId, activityId, student, comment, file, linkUrl);
    }

    @GetMapping("/{claseId}/activities/{activityId}/submissions")
    public List<ClaseSubmissionResponse> getActivitySubmissions(@PathVariable Long claseId,
            @PathVariable Long activityId,
            @AuthenticationPrincipal Usuario teacher) {
        return claseService.getSubmissions(claseId, activityId, teacher);
    }
}
