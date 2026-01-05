package com.cc.project.Controllers;

import com.cc.project.Entity.LabResult;
import com.cc.project.Entity.Lab;
import com.cc.project.Entity.User;
import com.cc.project.Entity.Doctor;
import com.cc.project.Entity.MedicalHistory;
import com.cc.project.Repository.LabRepository;
import com.cc.project.Repository.UserRepository;
import com.cc.project.Repository.DoctorRepository;
import com.cc.project.Repository.MedicalHistoryRepository;
import com.cc.project.Service.LabResultService;
import com.cc.project.Service.CloudinaryService;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lab-results")
public class LabResultController {
    private final LabResultService labResultService;
    private final CloudinaryService cloudinaryService;
    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

    public LabResultController(
            LabResultService labResultService,
            CloudinaryService cloudinaryService,
            LabRepository labRepository,
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            MedicalHistoryRepository medicalHistoryRepository) {
        this.labResultService = labResultService;
        this.cloudinaryService = cloudinaryService;
        this.labRepository = labRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
    }

    @GetMapping
    public List<LabResult> getAllResults() {
        return labResultService.getAllResults();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResult> getResultById(@PathVariable Long id) {
        return labResultService.getResultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LabResult sendResult(@RequestBody LabResult result) {
        return labResultService.saveResult(result);
    }

    // Upload a result file to Cloudinary and attach to existing LabResult
    @PostMapping(value = "/upload/{labResultId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LabResult> uploadResultFile(
            @PathVariable Long labResultId,
            @RequestParam("file") MultipartFile file) throws IOException {
        // Validate MIME type: allow Word (doc/docx), PDF, and images
        String contentType = file.getContentType();
        if (!isAllowedContentType(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported file type. Allowed: PDF, Word (.doc/.docx), and images.");
        }
        return labResultService.getResultById(labResultId)
                .map(existing -> {
                    try {
                        String url = cloudinaryService.uploadFile(file, "lab_results");
                        existing.setResultFileUrl(url);
                        if (existing.getDate() == null) {
                            existing.setDate(LocalDate.now());
                        }
                        LabResult saved = labResultService.saveResult(existing);
                        return ResponseEntity.ok(saved);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload lab result file: " + e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new lab result and upload the file in one request
    @PostMapping(value = "/create-with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LabResult> createWithFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("testName") String testName,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam("clientId") Long clientId,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("labId") Long labId,
            @RequestParam(value = "medicalHistoryId", required = false) Long medicalHistoryId) throws IOException {
        String contentType = file.getContentType();
        if (!isAllowedContentType(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported file type. Allowed: PDF, Word (.doc/.docx), and images.");
        }

        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lab not found"));
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor not found"));
        MedicalHistory medicalHistory = null;
        if (medicalHistoryId != null) {
            medicalHistory = medicalHistoryRepository.findById(medicalHistoryId)
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medical history not found"));
        }

        String url = cloudinaryService.uploadFile(file, "lab_results");

        LabResult result = new LabResult();
        result.setTestName(testName);
        result.setResultFileUrl(url);
        result.setDate(date != null && !date.isBlank() ? LocalDate.parse(date) : LocalDate.now());
        result.setLab(lab);
        result.setClient(client);
        result.setDoctor(doctor);
        if (medicalHistory != null) {
            result.setMedicalHistory(medicalHistory);
        }

        LabResult saved = labResultService.saveResult(result);
        return ResponseEntity.ok(saved);
    }

    private boolean isAllowedContentType(String contentType) {
        if (contentType == null)
            return false;
        // Images
        if (contentType.startsWith("image/"))
            return true;
        // PDF
        if ("application/pdf".equalsIgnoreCase(contentType))
            return true;
        // Word (doc, docx)
        if ("application/msword".equalsIgnoreCase(contentType))
            return true;
        if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(contentType))
            return true;
        return false;
    }

    @GetMapping("/client/{clientId}")
    public List<LabResult> getClientResults(@PathVariable Long clientId) {
        return labResultService.findByClient(clientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<LabResult> getDoctorResults(@PathVariable Long doctorId) {
        return labResultService.findByDoctor(doctorId);
    }

    @GetMapping("/lab/{labId}")
    public List<LabResult> getLabResults(@PathVariable Long labId) {
        return labResultService.findByLab(labId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        labResultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}