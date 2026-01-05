package com.cc.project.Controllers;

import com.cc.project.Entity.Appointment;
import com.cc.project.Entity.Doctor;
import com.cc.project.Entity.MedicalHistory;
import com.cc.project.Entity.User;
import com.cc.project.Repository.DoctorRepository;
import com.cc.project.Repository.MedicalHistoryRepository;
import com.cc.project.Repository.UserRepository;
import com.cc.project.Service.AppointmentService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

    public AppointmentController(
            AppointmentService appointmentService,
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            MedicalHistoryRepository medicalHistoryRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        return appointmentService.getAppointmentById(id)
                .map(existingAppointment -> {
                    existingAppointment.setStatus(Appointment.Status.valueOf(status));
                    Appointment updatedAppointment = appointmentService.saveAppointment(existingAppointment);
                    return ResponseEntity.ok(updatedAppointment);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Map<String, Object> payload) {
        Long clientId = Long.valueOf(payload.get("clientId").toString());
        Long doctorId = Long.valueOf(payload.get("doctorId").toString());
        String date = payload.get("date").toString();
        String time = payload.get("time").toString();
        String notes = payload.get("notes") != null ? payload.get("notes").toString() : "";

        User client = userRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setDoctor(doctor);
        appointment.setDate(LocalDate.parse(date));
        appointment.setTime(LocalTime.parse(time));
        appointment.setNotes(notes);
        appointment.setStatus(Appointment.Status.PENDING);

        Appointment saved = appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.findByDoctor(doctorId);
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public List<Appointment> getDoctorAppointmentsByDate(@PathVariable Long doctorId, @PathVariable String date) {
        return appointmentService.findByDoctorAndDate(doctorId, LocalDate.parse(date));
    }

    @GetMapping("/client/{clientId}")
    public List<Appointment> getClientAppointments(@PathVariable Long clientId) {
        return appointmentService.findByClient(clientId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}