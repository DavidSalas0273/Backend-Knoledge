package com.knoledge.backend.services;

import com.knoledge.backend.dto.ClaseActivityRequest;
import com.knoledge.backend.dto.ClaseActivityResponse;
import com.knoledge.backend.dto.ClaseEnrollmentResponse;
import com.knoledge.backend.dto.ClaseMaterialResponse;
import com.knoledge.backend.dto.ClaseRequest;
import com.knoledge.backend.dto.ClaseResponse;
import com.knoledge.backend.dto.ClaseSubmissionResponse;
import com.knoledge.backend.models.Clase;
import com.knoledge.backend.models.ClaseActivity;
import com.knoledge.backend.models.ClaseActivitySubmission;
import com.knoledge.backend.models.ClaseEnrollment;
import com.knoledge.backend.models.ClaseMaterial;
import com.knoledge.backend.models.EnrollmentStatus;
import com.knoledge.backend.models.Role;
import com.knoledge.backend.models.Usuario;
import com.knoledge.backend.repositories.ClaseActivityRepository;
import com.knoledge.backend.repositories.ClaseActivitySubmissionRepository;
import com.knoledge.backend.repositories.ClaseEnrollmentRepository;
import com.knoledge.backend.repositories.ClaseMaterialRepository;
import com.knoledge.backend.repositories.ClaseRepository;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClaseService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ClaseRepository claseRepository;
    private final ClaseMaterialRepository claseMaterialRepository;
    private final ClaseEnrollmentRepository claseEnrollmentRepository;
    private final ClaseActivityRepository claseActivityRepository;
    private final ClaseActivitySubmissionRepository claseActivitySubmissionRepository;
    private final FileStorageService fileStorageService;

    public ClaseService(ClaseRepository claseRepository,
            ClaseMaterialRepository claseMaterialRepository,
            ClaseEnrollmentRepository claseEnrollmentRepository,
            ClaseActivityRepository claseActivityRepository,
            ClaseActivitySubmissionRepository claseActivitySubmissionRepository,
            FileStorageService fileStorageService) {
        this.claseRepository = claseRepository;
        this.claseMaterialRepository = claseMaterialRepository;
        this.claseEnrollmentRepository = claseEnrollmentRepository;
        this.claseActivityRepository = claseActivityRepository;
        this.claseActivitySubmissionRepository = claseActivitySubmissionRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public ClaseResponse createClase(Usuario teacher, ClaseRequest request) {
        if (teacher == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        if (teacher.getRole() != Role.TEACHER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo los profesores pueden crear clases");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la clase es obligatorio");
        }
        if (request.getCourse() == null || request.getCourse().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona un curso base para tu clase");
        }
        if (request.getTeacherId() != null && !request.getTeacherId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No puedes crear clases en nombre de otro profesor");
        }

        Clase clase = new Clase();
        clase.setTeacher(teacher);
        clase.setName(request.getName().trim());
        clase.setDescription(request.getDescription());
        clase.setCourse(request.getCourse());
        clase.setStartDate(request.getStartDate());
        clase.setClassTime(request.getClassTime());
        if (request.getMaxStudents() != null && request.getMaxStudents() > 0) {
            clase.setMaxStudents(request.getMaxStudents());
        }
        if (request.getAutoApprove() != null) {
            clase.setAutoApprove(request.getAutoApprove());
        }
        if (request.getLateJoin() != null) {
            clase.setLateJoin(request.getLateJoin());
        }
        if (request.getNotifications() != null) {
            clase.setNotifications(request.getNotifications());
        }
        if (request.getPublicClass() != null) {
            clase.setPublicClass(request.getPublicClass());
        }
        if (request.getDurationMinutes() != null && request.getDurationMinutes() > 0) {
            clase.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getTopics() != null && !request.getTopics().isBlank()) {
            clase.setTopics(request.getTopics().trim());
        }

        clase.setCode(generateUniqueCode());

        Clase saved = claseRepository.save(clase);
        return toResponse(saved);
    }

    @Transactional
    public ClaseResponse updateClase(Long claseId, Usuario teacher, ClaseRequest request) {
        Clase clase = findOwnedClase(claseId, teacher);
        if (request.getName() != null && !request.getName().isBlank()) {
            clase.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            clase.setDescription(request.getDescription());
        }
        if (request.getCourse() != null) {
            clase.setCourse(request.getCourse());
        }
        if (request.getStartDate() != null) {
            clase.setStartDate(request.getStartDate());
        }
        if (request.getClassTime() != null) {
            clase.setClassTime(request.getClassTime());
        }
        if (request.getMaxStudents() != null && request.getMaxStudents() > 0) {
            clase.setMaxStudents(request.getMaxStudents());
        }
        if (request.getAutoApprove() != null) {
            clase.setAutoApprove(request.getAutoApprove());
        }
        if (request.getLateJoin() != null) {
            clase.setLateJoin(request.getLateJoin());
        }
        if (request.getNotifications() != null) {
            clase.setNotifications(request.getNotifications());
        }
        if (request.getPublicClass() != null) {
            clase.setPublicClass(request.getPublicClass());
        }
        if (request.getDurationMinutes() != null) {
            clase.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getTopics() != null) {
            clase.setTopics(request.getTopics());
        }
        Clase saved = claseRepository.save(clase);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ClaseResponse> getClasesForTeacher(Usuario teacher) {
        if (teacher == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        List<Clase> clases = claseRepository.findByTeacher(teacher);
        return clases.stream()
                .sorted(Comparator.comparing(Clase::getCreatedAt).reversed())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClaseResponse> getClasesForStudent(Usuario student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        List<ClaseEnrollment> enrollments = claseEnrollmentRepository.findByStudent(student);
        return enrollments.stream()
                .map(enrollment -> {
                    ClaseResponse response = toResponse(enrollment.getClase());
                    response.setEnrollmentStatus(enrollment.getStatus().name());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaseResponse joinClase(String code, Usuario student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        if (student.getRole() != Role.STUDENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo los estudiantes pueden unirse a clases");
        }
        if (code == null || code.trim().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código debe tener 6 caracteres");
        }
        String normalized = code.trim().toUpperCase();
        Clase clase = claseRepository.findByCode(normalized)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
        if (clase.getTeacher().getId().equals(student.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No puedes unirte a una clase que impartes como profesor");
        }
        if (claseEnrollmentRepository.existsByClaseAndStudent(clase, student)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya estás inscrito en esta clase");
        }
        ClaseEnrollment enrollment = new ClaseEnrollment();
        enrollment.setClase(clase);
        enrollment.setStudent(student);
        enrollment.setStatus(clase.isAutoApprove() ? EnrollmentStatus.APPROVED : EnrollmentStatus.PENDING);
        claseEnrollmentRepository.save(enrollment);

        ClaseResponse response = toResponse(clase);
        response.setEnrollmentStatus(enrollment.getStatus().name());
        return response;
    }

    @Transactional(readOnly = true)
    public List<ClaseEnrollmentResponse> getEnrollments(Long claseId, Usuario teacher) {
        Clase clase = findOwnedClase(claseId, teacher);
        return claseEnrollmentRepository.findByClase(clase).stream()
                .map(enrollment -> new ClaseEnrollmentResponse(
                        enrollment.getId(),
                        enrollment.getStudent().getId(),
                        enrollment.getStudent().getName(),
                        enrollment.getStudent().getEmail(),
                        enrollment.getStatus(),
                        enrollment.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void leaveClase(Long claseId, Usuario student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
        ClaseEnrollment enrollment = claseEnrollmentRepository.findByClaseAndStudent(clase, student)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No estás inscrito en esta clase"));
        claseEnrollmentRepository.delete(enrollment);
    }

    @Transactional(readOnly = true)
    public ClaseResponse getClaseDetail(Long claseId, Usuario requester) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
        if (requester == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        ClaseResponse response = toResponse(clase);
        if (requester.getRole() == Role.TEACHER) {
            if (!clase.getTeacher().getId().equals(requester.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes acceder a esta clase");
            }
            response.setEnrollmentStatus("OWNER");
            return response;
        }
        if (requester.getRole() == Role.STUDENT) {
            ClaseEnrollment enrollment = claseEnrollmentRepository
                    .findByClaseAndStudent(clase, requester)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No estás inscrito"));
            response.setEnrollmentStatus(enrollment.getStatus().name());
            return response;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol no soportado");
    }

    @Transactional(readOnly = true)
    public List<ClaseActivityResponse> getActivities(Long claseId, Usuario requester) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
        ensureAccessToClase(clase, requester);
        List<ClaseActivity> activities = claseActivityRepository.findByClaseOrderByCreatedAtDesc(clase);
        return activities.stream()
                .map(activity -> new ClaseActivityResponse(
                        activity.getId(),
                        activity.getTitle(),
                        activity.getDescription(),
                        activity.getDueDate(),
                        activity.getYoutubeUrl(),
                        activity.getCreatedAt(),
                        claseActivitySubmissionRepository.countByActivity(activity)))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaseActivityResponse createActivity(Long claseId, Usuario teacher, ClaseActivityRequest request) {
        Clase clase = findOwnedClase(claseId, teacher);
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }
        ClaseActivity activity = new ClaseActivity();
        activity.setClase(clase);
        activity.setTitle(request.getTitle().trim());
        activity.setDescription(request.getDescription());
        activity.setDueDate(request.getDueDate());
        if (request.getYoutubeUrl() != null && !request.getYoutubeUrl().isBlank()) {
            activity.setYoutubeUrl(sanitizeLink(request.getYoutubeUrl()));
        }
        ClaseActivity saved = claseActivityRepository.save(activity);
        return new ClaseActivityResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getDueDate(),
                saved.getYoutubeUrl(),
                saved.getCreatedAt(),
                0);
    }

    @Transactional
    public ClaseSubmissionResponse submitActivity(Long claseId, Long activityId, Usuario student,
            String comment, MultipartFile file, String linkUrl) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
        ensureStudentEnrollment(clase, student);

        ClaseActivity activity = claseActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada"));
        if (!activity.getClase().getId().equals(clase.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La actividad no pertenece a la clase");
        }

        boolean hasFile = file != null && !file.isEmpty();
        boolean hasLink = linkUrl != null && !linkUrl.isBlank();
        if (!hasFile && !hasLink && (comment == null || comment.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes adjuntar un archivo, enlace o comentario");
        }

        ClaseActivitySubmission submission = new ClaseActivitySubmission();
        submission.setActivity(activity);
        submission.setStudent(student);
        submission.setComment(comment);
        if (hasLink) {
            submission.setLinkUrl(sanitizeLink(linkUrl));
        }
        if (hasFile) {
            try {
                String storedPath = fileStorageService.store(file, "classes/" + clase.getId() + "/activities");
                submission.setStoredFileName(storedPath);
                submission.setOriginalFileName(file.getOriginalFilename());
                submission.setFileUrl("/files/" + storedPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar el archivo", e);
            }
        }
        ClaseActivitySubmission saved = claseActivitySubmissionRepository.save(submission);
        return toSubmissionResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ClaseSubmissionResponse> getSubmissions(Long claseId, Long activityId, Usuario teacher) {
        Clase clase = findOwnedClase(claseId, teacher);
        ClaseActivity activity = claseActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada"));
        if (!activity.getClase().getId().equals(clase.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La actividad no pertenece a esta clase");
        }
        List<ClaseActivitySubmission> submissions = claseActivitySubmissionRepository
                .findByActivityOrderByCreatedAtDesc(activity);
        return submissions.stream()
                .map(this::toSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaseMaterialResponse addMaterial(Long claseId, Usuario teacher, MultipartFile file,
            String linkUrl, String linkTitle) {
        Clase clase = findOwnedClase(claseId, teacher);
        boolean hasFile = file != null && !file.isEmpty();
        boolean hasLink = linkUrl != null && !linkUrl.isBlank();
        if (!hasFile && !hasLink) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes adjuntar un archivo o proporcionar un enlace");
        }

        ClaseMaterial material = new ClaseMaterial();
        material.setClase(clase);

        if (hasFile) {
            try {
                String storedPath = fileStorageService.store(file, "classes/" + clase.getId());
                material.setOriginalName(file.getOriginalFilename());
                material.setStoredName(storedPath);
                material.setContentType(file.getContentType() != null ? file.getContentType()
                        : "application/octet-stream");
                material.setUrl("/files/" + storedPath);
                material.setExternal(false);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No se pudo almacenar el archivo", e);
            }
        } else {
            String sanitized = sanitizeLink(linkUrl);
            material.setOriginalName(linkTitle != null && !linkTitle.isBlank() ? linkTitle.trim() : sanitized);
            material.setStoredName("external-" + UUID.randomUUID());
            material.setContentType("text/uri-list");
            material.setUrl(sanitized);
            material.setExternal(true);
        }

        ClaseMaterial saved = claseMaterialRepository.save(material);
        return toMaterialResponse(saved);
    }

    private Clase findOwnedClase(Long claseId, Usuario teacher) {
        if (teacher == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
        if (!clase.getTeacher().getId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para esta clase");
        }
        return clase;
    }

    private ClaseResponse toResponse(Clase clase) {
        List<ClaseMaterialResponse> materials = clase.getMaterials().stream()
                .sorted(Comparator.comparing(ClaseMaterial::getUploadedAt).reversed())
                .map(this::toMaterialResponse)
                .collect(Collectors.toList());
        ClaseResponse response = new ClaseResponse(
                clase.getId(),
                clase.getName(),
                clase.getDescription(),
                clase.getCourse(),
                clase.getStartDate(),
                clase.getClassTime(),
                clase.getMaxStudents(),
                clase.isAutoApprove(),
                clase.isLateJoin(),
                clase.isNotifications(),
                clase.isPublicClass(),
                clase.getDurationMinutes(),
                clase.getTopics(),
                clase.getCode(),
                clase.getCreatedAt(),
                materials);
        if (clase.getTeacher() != null) {
            response.setTeacherId(clase.getTeacher().getId());
            response.setTeacherName(clase.getTeacher().getName());
            response.setTeacherEmail(clase.getTeacher().getEmail());
        }
        return response;
    }

    private ClaseMaterialResponse toMaterialResponse(ClaseMaterial material) {
        return new ClaseMaterialResponse(
                material.getId(),
                material.getOriginalName(),
                material.getContentType(),
                material.getUrl(),
                material.isExternal(),
                material.getUploadedAt());
    }

    private ClaseSubmissionResponse toSubmissionResponse(ClaseActivitySubmission submission) {
        return new ClaseSubmissionResponse(
                submission.getId(),
                submission.getActivity().getId(),
                submission.getStudent().getId(),
                submission.getStudent().getName(),
                submission.getStudent().getEmail(),
                submission.getComment(),
                submission.getFileUrl(),
                submission.getOriginalFileName(),
                submission.getLinkUrl(),
                submission.getCreatedAt());
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = randomCode();
        } while (claseRepository.existsByCode(code));
        return code;
    }

    private String randomCode() {
        StringBuilder builder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = RANDOM.nextInt(CODE_CHARS.length());
            builder.append(CODE_CHARS.charAt(index));
        }
        return builder.toString();
    }

    private String sanitizeLink(String raw) {
        String trimmed = raw.trim();
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            trimmed = "https://" + trimmed;
        }
        return trimmed;
    }

    private void ensureAccessToClase(Clase clase, Usuario user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        if (user.getRole() == Role.TEACHER) {
            if (!clase.getTeacher().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes acceder a esta clase");
            }
            return;
        }
        if (user.getRole() == Role.STUDENT) {
            ensureStudentEnrollment(clase, user);
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol no soportado");
    }

    private void ensureStudentEnrollment(Clase clase, Usuario student) {
        boolean enrolled = claseEnrollmentRepository.existsByClaseAndStudent(clase, student);
        if (!enrolled) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No estás inscrito en esta clase");
        }
    }
}
