package com.cc.project.Service;

import com.cc.project.Entity.Appointment;
import com.cc.project.Repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment saveAppointment(Appointment appointment) {
        Appointment savedAppointment = appointmentRepository.save(appointment);

        String dateStr = savedAppointment.getDate().toString() + " at " + savedAppointment.getTime().toString();

        // Send notification to doctor if appointment is requested (PENDING)
        if (savedAppointment.getStatus() == Appointment.Status.PENDING &&
                savedAppointment.getDoctor() != null &&
                savedAppointment.getDoctor().getUser() != null &&
                savedAppointment.getClient() != null) {
            try {
                notificationService.createAppointmentRequestNotification(
                        savedAppointment.getDoctor().getUser(),
                        savedAppointment.getId(),
                        savedAppointment.getClient().getUsername(),
                        dateStr);
                System.out.println("Doctor notification created for appointment request.");
            } catch (Exception e) {
                // Log error but don't fail the appointment save
                System.err.println("Failed to create doctor notification: " + e.getMessage());
            }
        }

        // Send notification to patient if appointment is confirmed
        if (savedAppointment.getStatus() == Appointment.Status.CONFIRMED && savedAppointment.getClient() != null) {
            try {
                notificationService.createAppointmentAcceptedNotification(
                        savedAppointment.getClient(),
                        savedAppointment.getId(),
                        dateStr);
            } catch (Exception e) {
                // Log error but don't fail the appointment save
                System.err.println("Failed to create patient notification: " + e.getMessage());
            }
        }

        return savedAppointment;
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> findByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> findByClient(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    public List<Appointment> findByDoctorAndDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }
}