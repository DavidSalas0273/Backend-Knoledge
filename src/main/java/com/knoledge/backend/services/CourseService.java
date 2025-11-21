package com.knoledge.backend.services;

import com.knoledge.backend.dto.AssignmentRequest;
import com.knoledge.backend.dto.AssignmentResponse;
import com.knoledge.backend.dto.AssignmentSubmissionResponse;
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
import com.knoledge.backend.models.Assignment;
import com.knoledge.backend.models.AssignmentSubmission;
import com.knoledge.backend.models.Course;
import com.knoledge.backend.models.CourseEnrollment;
import com.knoledge.backend.models.CourseMaterial;
import com.knoledge.backend.models.EnrollmentStatus;
import com.knoledge.backend.models.Usuario;
import com.knoledge.backend.repositories.AssignmentRepository;
import com.knoledge.backend.repositories.AssignmentSubmissionRepository;
import com.knoledge.backend.repositories.CourseEnrollmentRepository;
import com.knoledge.backend.repositories.CourseMaterialRepository;
import com.knoledge.backend.repositories.CourseRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMaterialRepository courseMaterialRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final FileStorageService fileStorageService;

    public CourseService(CourseRepository courseRepository,
            CourseMaterialRepository courseMaterialRepository,
            CourseEnrollmentRepository courseEnrollmentRepository,
            AssignmentRepository assignmentRepository,
            AssignmentSubmissionRepository assignmentSubmissionRepository,
            FileStorageService fileStorageService) {
        this.courseRepository = courseRepository;
        this.courseMaterialRepository = courseMaterialRepository;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.assignmentRepository = assignmentRepository;
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public CourseResponse createCourse(Usuario teacher, CourseRequest request) {
        Course course = new Course();
        course.setTeacher(teacher);
        course.setTitle(request.getTitle());
        course.setCategory(request.getCategory());
        course.setDescription(request.getDescription());
        course.setContent(request.getContent());
        course.setTopics(request.getTopics());
        course.setDurationMinutes(request.getDurationMinutes());
        course.setJoinCode(generateJoinCode());
        Course saved = courseRepository.save(course);
        return CourseResponse.fromEntity(saved);
    }

    public List<TeacherCourseSummaryResponse> getCoursesForTeacher(Usuario teacher) {
        return courseRepository.findByTeacherOrderByCreatedAtDesc(teacher)
                .stream()
                .map(course -> TeacherCourseSummaryResponse.of(course,
                        courseEnrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.PENDING),
                        courseEnrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.APPROVED)))
                .collect(Collectors.toList());
    }

    public CourseDetailResponse getCourseDetailForTeacher(Long courseId, Usuario teacher) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        return buildCourseDetail(course);
    }

    public CourseDetailResponse getCourseDetailForStudent(Long courseId, Usuario student) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        CourseEnrollment enrollment = courseEnrollmentRepository.findByCourseAndStudent(course, student)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No estás inscrito"));
        if (enrollment.getStatus() != EnrollmentStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tu inscripción está pendiente");
        }
        return buildCourseDetail(course);
    }

    public List<StudentCourseSummaryResponse> getCoursesForStudent(Usuario student) {
        return courseEnrollmentRepository.findByStudent(student)
                .stream()
                .map(StudentCourseSummaryResponse::fromEnrollment)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseMaterialResponse addMaterial(Long courseId, Usuario teacher,
            CourseMaterialRequest request, MultipartFile file) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        try {
            CourseMaterial material = new CourseMaterial();
            material.setCourse(course);
            material.setType(request.getType());
            material.setTitle(request.getTitle());
            material.setDescription(request.getDescription());
            if (request.getType() == com.knoledge.backend.models.CourseMaterialType.VIDEO_LINK) {
                if (request.getResourceUrl() == null || request.getResourceUrl().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes proporcionar un enlace");
                }
                material.setResourceUrl(request.getResourceUrl());
            } else {
                if (file == null || file.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes adjuntar un archivo");
                }
                String stored = fileStorageService.store(file, "materials");
                material.setFilePath(stored);
            }
            CourseMaterial saved = courseMaterialRepository.save(material);
            return CourseMaterialResponse.fromEntity(saved, saved.getFilePath());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Transactional
    public AssignmentResponse createAssignment(Long courseId, Usuario teacher, AssignmentRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        if (request.getDueDate() != null) {
            assignment.setDueDate(LocalDateTime.parse(request.getDueDate()));
        }
        Assignment saved = assignmentRepository.save(assignment);
        return AssignmentResponse.fromEntity(saved, List.of());
    }

    public JoinCourseResponse joinCourse(JoinCourseRequest request, Usuario student) {
        String code = request.getCode().trim().toUpperCase(Locale.ROOT);
        Course course = courseRepository.findByJoinCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código inválido"));
        if (course.getTeacher().getId().equals(student.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes unirte a tu curso");
        }
        CourseEnrollment enrollment = courseEnrollmentRepository.findByCourseAndStudent(course, student)
                .orElseGet(() -> {
                    CourseEnrollment ce = new CourseEnrollment();
                    ce.setCourse(course);
                    ce.setStudent(student);
                    return ce;
                });
        if (enrollment.getStatus() == EnrollmentStatus.APPROVED) {
            return new JoinCourseResponse(course.getId(), EnrollmentStatus.APPROVED, "Ya eres parte de esta clase");
        }
        enrollment.setStatus(EnrollmentStatus.PENDING);
        courseEnrollmentRepository.save(enrollment);
        return new JoinCourseResponse(course.getId(), EnrollmentStatus.PENDING, "Solicitud enviada, espera aprobación");
    }

    @Transactional
    public AssignmentSubmissionResponse submitAssignment(Long assignmentId, Usuario student, MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));
        CourseEnrollment enrollment = courseEnrollmentRepository.findByCourseAndStudent(assignment.getCourse(), student)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No perteneces a esta clase"));
        if (enrollment.getStatus() != EnrollmentStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tu inscripción está pendiente");
        }
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adjunta un archivo");
        }
        try {
            String stored = fileStorageService.store(file, "submissions");
            AssignmentSubmission submission = new AssignmentSubmission();
            submission.setAssignment(assignment);
            submission.setStudent(student);
            submission.setFilePath(stored);
            AssignmentSubmission saved = assignmentSubmissionRepository.save(submission);
            return AssignmentSubmissionResponse.fromEntity(saved, saved.getFilePath());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Transactional
    public void gradeSubmission(Long assignmentId, Long submissionId, Usuario teacher, Double grade, String feedback) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));
        if (!assignment.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        AssignmentSubmission submission = assignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrega no encontrada"));
        submission.setGrade(grade);
        submission.setFeedback(feedback);
        assignmentSubmissionRepository.save(submission);
    }

    @Transactional
    public void approveEnrollment(Long enrollmentId, Usuario teacher, boolean approve) {
        CourseEnrollment enrollment = courseEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscripción no encontrada"));
        if (!enrollment.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        enrollment.setStatus(approve ? EnrollmentStatus.APPROVED : EnrollmentStatus.REJECTED);
        courseEnrollmentRepository.save(enrollment);
    }

    @Transactional
    public void deleteCourse(Long courseId, Usuario teacher) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        List<CourseMaterial> materials = courseMaterialRepository.findByCourse(course);
        materials.forEach(material -> fileStorageService.delete(material.getFilePath()));
        courseMaterialRepository.deleteAll(materials);

        List<Assignment> assignments = assignmentRepository.findByCourse(course);
        assignments.forEach(assignment -> {
            List<AssignmentSubmission> submissions = assignmentSubmissionRepository.findByAssignment(assignment);
            submissions.forEach(submission -> fileStorageService.delete(submission.getFilePath()));
            assignmentSubmissionRepository.deleteAll(submissions);
        });
        assignmentRepository.deleteAll(assignments);

        List<CourseEnrollment> enrollments = courseEnrollmentRepository.findByCourse(course);
        courseEnrollmentRepository.deleteAll(enrollments);
        courseRepository.delete(course);
    }

    private CourseDetailResponse buildCourseDetail(Course course) {
        List<CourseMaterialResponse> materials = courseMaterialRepository.findByCourse(course)
                .stream()
                .map(material -> CourseMaterialResponse.fromEntity(material, material.getFilePath()))
                .collect(Collectors.toList());
        List<EnrollmentResponse> pending = courseEnrollmentRepository.findByCourseAndStatus(course, EnrollmentStatus.PENDING)
                .stream().map(EnrollmentResponse::fromEntity).collect(Collectors.toList());
        List<EnrollmentResponse> approved = courseEnrollmentRepository.findByCourseAndStatus(course, EnrollmentStatus.APPROVED)
                .stream().map(EnrollmentResponse::fromEntity).collect(Collectors.toList());
        List<AssignmentResponse> assignments = assignmentRepository.findByCourse(course)
                .stream()
                .map(assignment -> {
                    List<AssignmentSubmissionResponse> submissions = assignmentSubmissionRepository.findByAssignment(assignment)
                            .stream()
                            .map(submission -> AssignmentSubmissionResponse.fromEntity(submission, submission.getFilePath()))
                            .collect(Collectors.toList());
                    return AssignmentResponse.fromEntity(assignment, submissions);
                })
                .collect(Collectors.toList());
        return new CourseDetailResponse(CourseResponse.fromEntity(course), materials, pending, approved, assignments);
    }

    private String generateJoinCode() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
    }
}
