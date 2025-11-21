package com.knoledge.backend.controllers;

import com.knoledge.backend.dto.AssignmentRequest;
import com.knoledge.backend.dto.CourseDetailResponse;
import com.knoledge.backend.dto.CourseMaterialRequest;
import com.knoledge.backend.dto.CourseMaterialResponse;
import com.knoledge.backend.dto.CourseRequest;
import com.knoledge.backend.dto.CourseResponse;
import com.knoledge.backend.dto.EnrollmentResponse;
import com.knoledge.backend.dto.JoinCourseRequest;
import com.knoledge.backend.dto.JoinCourseResponse;
import com.knoledge.backend.dto.StudentCourseSummaryResponse;
import com.knoledge.backend.dto.TeacherCourseSummaryResponse;
import com.knoledge.backend.models.Usuario;
import com.knoledge.backend.repositories.UsuarioRepository;
import com.knoledge.backend.services.CourseService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@AuthenticationPrincipal Usuario teacher,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(teacher, request));
    }

    @GetMapping("/teacher")
    public List<TeacherCourseSummaryResponse> getTeacherCourses(@AuthenticationPrincipal Usuario teacher) {
        return courseService.getCoursesForTeacher(teacher);
    }

    @GetMapping("/student")
    public List<StudentCourseSummaryResponse> getStudentCourses(@AuthenticationPrincipal Usuario student) {
        return courseService.getCoursesForStudent(student);
    }

    @GetMapping("/{courseId}/teacher")
    public CourseDetailResponse getCourseDetailForTeacher(@PathVariable Long courseId,
            @AuthenticationPrincipal Usuario teacher) {
        return courseService.getCourseDetailForTeacher(courseId, teacher);
    }

    @GetMapping("/{courseId}/student")
    public CourseDetailResponse getCourseDetailForStudent(@PathVariable Long courseId,
            @AuthenticationPrincipal Usuario student) {
        return courseService.getCourseDetailForStudent(courseId, student);
    }

    @PostMapping("/{courseId}/materials")
    public CourseMaterialResponse addMaterial(@PathVariable Long courseId,
            @AuthenticationPrincipal Usuario teacher,
            @RequestPart("metadata") @Valid CourseMaterialRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return courseService.addMaterial(courseId, teacher, request, file);
    }

    @PostMapping("/{courseId}/assignments")
    public ResponseEntity<?> createAssignment(@PathVariable Long courseId,
            @AuthenticationPrincipal Usuario teacher,
            @Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.ok(courseService.createAssignment(courseId, teacher, request));
    }

    @PostMapping("/join")
    public JoinCourseResponse joinCourse(@AuthenticationPrincipal Usuario student,
            @Valid @RequestBody JoinCourseRequest request) {
        return courseService.joinCourse(request, student);
    }

    @PostMapping("/enrollments/{enrollmentId}/approve")
    public ResponseEntity<Void> approveEnrollment(@PathVariable Long enrollmentId,
            @AuthenticationPrincipal Usuario teacher) {
        courseService.approveEnrollment(enrollmentId, teacher, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/enrollments/{enrollmentId}/reject")
    public ResponseEntity<Void> rejectEnrollment(@PathVariable Long enrollmentId,
            @AuthenticationPrincipal Usuario teacher) {
        courseService.approveEnrollment(enrollmentId, teacher, false);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId,
            @AuthenticationPrincipal Usuario teacher) {
        courseService.deleteCourse(courseId, teacher);
        return ResponseEntity.noContent().build();
    }
}
