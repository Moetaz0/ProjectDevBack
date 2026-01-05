package com.cc.project.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cc.project.Entity.*;
import com.cc.project.Service.PrescriptionService;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // --------------------------
    // PATIENT: Get own prescriptions
    // --------------------------
    @GetMapping("/patient/{patientId}")
    public List<Prescription> getPatientPrescriptions(@PathVariable Long patientId) {
        return prescriptionService.getPatientPrescriptions(patientId);
    }

    // --------------------------
    // DOCTOR: Get all prescriptions the doctor wrote
    // --------------------------
    @GetMapping("/doctor/{doctorId}")
    public List<Prescription> getDoctorPrescriptions(@PathVariable Long doctorId) {
        return prescriptionService.getDoctorPrescriptions(doctorId);
    }

    // --------------------------
    // DOCTOR: Create prescription
    // --------------------------
    @PostMapping("/create/{doctorId}/{patientId}")
    public Prescription createPrescription(
            @PathVariable Long doctorId,
            @PathVariable Long patientId,
            @RequestBody Prescription prescription) {

        return prescriptionService.createPrescription(doctorId, patientId, prescription);
    }

    // --------------------------
    // PATIENT: Request refill
    // --------------------------
    @PostMapping("/refill/{prescriptionId}/{patientId}")
    public RefillRequest requestRefill(
            @PathVariable Long prescriptionId,
            @PathVariable Long patientId) {

        return prescriptionService.requestRefill(prescriptionId, patientId);
    }

    @GetMapping("/refill/{patientId}")
    public List<RefillRequest> getAllLatestRefill(@PathVariable Long patientId) {
        return prescriptionService.getAllPatientRefillRequests(patientId);
    }

    // --------------------------
    // DOCTOR: Approve or Deny refill
    // --------------------------
    @PutMapping("/refill/update/{refillId}/{doctorId}")
    public RefillRequest updateRefill(
            @PathVariable Long refillId,
            @PathVariable Long doctorId,
            @RequestBody RefillDecisionRequest body) {

        if (body == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }

        Boolean decision = body.getApprove();
        if (decision == null) {
            decision = body.getStatus();
        }

        if (decision == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "approve or status is required");
        }

        return prescriptionService.updateRefill(refillId, decision, doctorId);
    }

    // Simple DTO to capture the approve flag from JSON body.
    public static class RefillDecisionRequest {
        private Boolean approve;
        private Boolean status;

        public Boolean getApprove() {
            return approve;
        }

        public void setApprove(Boolean approve) {
            this.approve = approve;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
}
