package com.cc.project.Service;

import com.cc.project.Entity.Appointment;
import com.cc.project.Repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentReminderService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    /**
     * Scheduled task that runs every minute to check for upcoming appointments
     * and send reminder notifications in near real-time without duplication.
     */
    @Scheduled(cron = "0 * * * * ?") // Runs every minute
    public void sendAppointmentReminders() {
        log.info("Starting appointment reminder check...");

        LocalDate today = LocalDate.now();
        LocalDate threeDaysFromNow = today.plusDays(3);
        LocalDate oneDayFromNow = today.plusDays(1);

        // Send 3-day reminders
        send3DayReminders(threeDaysFromNow);

        // Send 1-day reminders
        send1DayReminders(oneDayFromNow);

        log.info("Appointment reminder check completed.");
    }

    /**
     * Send reminders for appointments 3 days from now
     */
    private void send3DayReminders(LocalDate targetDate) {
        List<Appointment> appointments = appointmentRepository.findByDate(targetDate);

        for (Appointment appointment : appointments) {
            // Only send reminders for confirmed appointments
            if (appointment.getStatus() == Appointment.Status.CONFIRMED && appointment.getClient() != null) {
                try {
                    String dateStr = appointment.getDate().toString() + " at " + appointment.getTime().toString();
                    // Avoid duplicate notifications if scheduler runs frequently
                    boolean alreadySent = notificationService.hasNotificationForAppointment(
                            appointment.getClient().getId(),
                            appointment.getId(),
                            com.cc.project.Entity.Notification.NotificationType.APPOINTMENT_REMINDER_3_DAYS);
                    if (!alreadySent) {
                        notificationService.createAppointmentReminder3Days(
                                appointment.getClient(),
                                appointment.getId(),
                                dateStr);
                        log.info("Sent 3-day reminder to user {} for appointment {}",
                                appointment.getClient().getId(), appointment.getId());
                    } else {
                        log.debug("3-day reminder already exists for user {} and appointment {}",
                                appointment.getClient().getId(), appointment.getId());
                    }
                } catch (Exception e) {
                    log.error("Failed to send 3-day reminder for appointment {}: {}",
                            appointment.getId(), e.getMessage());
                }
            }
        }
    }

    /**
     * Send reminders for appointments 1 day from now
     */
    private void send1DayReminders(LocalDate targetDate) {
        List<Appointment> appointments = appointmentRepository.findByDate(targetDate);

        for (Appointment appointment : appointments) {
            // Only send reminders for confirmed appointments
            if (appointment.getStatus() == Appointment.Status.CONFIRMED && appointment.getClient() != null) {
                try {
                    String dateStr = appointment.getDate().toString() + " at " + appointment.getTime().toString();
                    // Avoid duplicate notifications if scheduler runs frequently
                    boolean alreadySent = notificationService.hasNotificationForAppointment(
                            appointment.getClient().getId(),
                            appointment.getId(),
                            com.cc.project.Entity.Notification.NotificationType.APPOINTMENT_REMINDER_1_DAY);
                    if (!alreadySent) {
                        notificationService.createAppointmentReminder1Day(
                                appointment.getClient(),
                                appointment.getId(),
                                dateStr);
                        log.info("Sent 1-day reminder to user {} for appointment {}",
                                appointment.getClient().getId(), appointment.getId());
                    } else {
                        log.debug("1-day reminder already exists for user {} and appointment {}",
                                appointment.getClient().getId(), appointment.getId());
                    }
                } catch (Exception e) {
                    log.error("Failed to send 1-day reminder for appointment {}: {}",
                            appointment.getId(), e.getMessage());
                }
            }
        }
    }

    /**
     * Manual trigger for testing purposes
     */
    public void triggerRemindersManually() {
        log.info("Manually triggering appointment reminders...");
        sendAppointmentReminders();
    }
}
