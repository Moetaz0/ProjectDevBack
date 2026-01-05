package com.cc.project.Service;

import com.cc.project.Entity.Notification;
import com.cc.project.Entity.User;
import com.cc.project.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    /**
     * Create a new notification
     */
    @Transactional
    public Notification createNotification(User user, String message, Notification.NotificationType type,
            Long appointmentId, Long prescriptionId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setAppointmentId(appointmentId);
        notification.setPrescriptionId(prescriptionId);
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }

    /**
     * Get all notifications for a user
     */
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
    }

    /**
     * Get unread notification count
     */
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    /**
     * Check whether a notification for a specific appointment and type already
     * exists
     */
    public boolean hasNotificationForAppointment(Long userId, Long appointmentId, Notification.NotificationType type) {
        return notificationRepository.existsByUserIdAndAppointmentIdAndType(userId, appointmentId, type);
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            notification.get().setIsRead(true);
            return notificationRepository.save(notification.get());
        }
        throw new RuntimeException("Notification not found");
    }

    /**
     * Mark all notifications as read for a user
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Delete a notification
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    /**
     * Helper method to create prescription notification
     */
    @Transactional
    public Notification createPrescriptionNotification(User patient, Long prescriptionId, String medicineName) {
        String message = String.format("A new prescription for %s has been added by your doctor.", medicineName);
        Notification notification = createNotification(patient, message,
                Notification.NotificationType.PRESCRIPTION_ADDED,
                null, prescriptionId);

        // Send email notification
        try {
            emailService.sendPrescriptionNotificationEmail(
                    patient.getEmail(),
                    patient.getUsername(),
                    medicineName,
                    prescriptionId);
        } catch (Exception e) {
            System.err.println("Failed to send prescription notification email: " + e.getMessage());
        }

        return notification;
    }

    /**
     * Helper method to create appointment acceptance notification
     */
    @Transactional
    public Notification createAppointmentAcceptedNotification(User patient, Long appointmentId,
            String appointmentDate) {
        String message = String.format("Your appointment on %s has been confirmed.", appointmentDate);
        Notification notification = createNotification(patient, message,
                Notification.NotificationType.APPOINTMENT_ACCEPTED,
                appointmentId, null);

        // Send email notification
        try {
            emailService.sendAppointmentConfirmedEmail(
                    patient.getEmail(),
                    patient.getUsername(),
                    appointmentDate,
                    appointmentId);
        } catch (Exception e) {
            System.err.println("Failed to send appointment confirmed email: " + e.getMessage());
        }

        return notification;
    }

    /**
     * Helper method to create appointment reminder notification (3 days)
     */
    @Transactional
    public Notification createAppointmentReminder3Days(User patient, Long appointmentId, String appointmentDate) {
        String message = String.format("Reminder: You have an appointment in 3 days on %s.", appointmentDate);
        Notification notification = createNotification(patient, message,
                Notification.NotificationType.APPOINTMENT_REMINDER_3_DAYS,
                appointmentId, null);

        // Send email notification
        try {
            emailService.sendAppointmentReminderEmail(
                    patient.getEmail(),
                    patient.getUsername(),
                    appointmentDate,
                    appointmentId,
                    "3 days");
        } catch (Exception e) {
            System.err.println("Failed to send 3-day appointment reminder email: " + e.getMessage());
        }

        return notification;
    }

    /**
     * Helper method to create appointment reminder notification (1 day)
     */
    @Transactional
    public Notification createAppointmentReminder1Day(User patient, Long appointmentId, String appointmentDate) {
        String message = String.format("Reminder: You have an appointment tomorrow on %s.", appointmentDate);
        Notification notification = createNotification(patient, message,
                Notification.NotificationType.APPOINTMENT_REMINDER_1_DAY,
                appointmentId, null);

        // Send email notification
        try {
            emailService.sendAppointmentReminderEmail(
                    patient.getEmail(),
                    patient.getUsername(),
                    appointmentDate,
                    appointmentId,
                    "tomorrow");
        } catch (Exception e) {
            System.err.println("Failed to send 1-day appointment reminder email: " + e.getMessage());
        }

        return notification;
    }

    /**
     * Helper method to create appointment request notification for doctor
     */
    @Transactional
    public Notification createAppointmentRequestNotification(User doctor, Long appointmentId,
            String patientName, String appointmentDate) {
        String message = String.format("New appointment request from %s on %s.", patientName, appointmentDate);
        Notification notification = createNotification(doctor, message,
                Notification.NotificationType.APPOINTMENT_REQUESTED,
                appointmentId, null);

        // Send email notification
        try {
            emailService.sendAppointmentRequestEmail(
                    doctor.getEmail(),
                    doctor.getUsername(),
                    patientName,
                    appointmentDate,
                    appointmentId);
        } catch (Exception e) {
            System.err.println("Failed to send appointment request email to doctor: " + e.getMessage());
        }

        return notification;
    }
}
