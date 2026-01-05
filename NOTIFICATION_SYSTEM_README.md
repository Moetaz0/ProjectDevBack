# Notification System - Implementation Summary

## Overview

A comprehensive notification system has been implemented for the MedLink application to notify users about:

- New prescriptions
- Accepted appointments
- Appointment reminders (3 days and 1 day before)

## Components Created

### 1. Entity Layer

**File:** `Notification.java`

- Stores notification details
- Fields: id, user, message, type, isRead, createdAt, appointmentId, prescriptionId
- NotificationType enum: PRESCRIPTION_ADDED, APPOINTMENT_ACCEPTED, APPOINTMENT_REMINDER_3_DAYS, APPOINTMENT_REMINDER_1_DAY

### 2. Repository Layer

**File:** `NotificationRepository.java`

- Methods for finding notifications by user
- Filter by read/unread status
- Count unread notifications

### 3. Service Layer

#### NotificationService.java

- Create notifications
- Get user notifications (all/unread)
- Mark as read (single/all)
- Delete notifications
- Helper methods for specific notification types

#### AppointmentReminderService.java

- Scheduled task running daily at 9:00 AM
- Sends 3-day and 1-day appointment reminders
- Only sends for CONFIRMED appointments
- Cron expression: "0 0 9 \* \* ?" (9 AM daily)

### 4. Controller Layer

**File:** `NotificationController.java`

#### Endpoints:

```
GET    /api/notifications/user/{userId}              - Get all notifications
GET    /api/notifications/user/{userId}/unread       - Get unread notifications
GET    /api/notifications/user/{userId}/unread/count - Get unread count
PUT    /api/notifications/{notificationId}/read      - Mark as read
PUT    /api/notifications/user/{userId}/read-all     - Mark all as read
DELETE /api/notifications/{notificationId}           - Delete notification
```

## Integration with Existing Services

### PrescriptionService

- Modified `createPrescription()` method
- Automatically creates notification when prescription is added
- Notification sent to patient

### AppointmentService

- Modified `saveAppointment()` method
- Creates notification when appointment status is CONFIRMED
- Notification sent to client/patient

### AppointmentRepository

- Added `findByDate(LocalDate date)` method for reminder queries

## Configuration

### ProjectApplication.java

- Added `@EnableScheduling` annotation to enable scheduled tasks

## How It Works

### 1. Prescription Notifications

When a doctor creates a prescription:

1. Prescription is saved
2. Notification is created for the patient
3. Message: "A new prescription for {medicineName} has been added by your doctor."

### 2. Appointment Acceptance Notifications

When an appointment is confirmed:

1. Appointment status is set to CONFIRMED
2. Notification is created for the patient
3. Message: "Your appointment on {date} at {time} has been confirmed."

### 3. Appointment Reminders

The scheduler runs daily at 9:00 AM and:

1. Queries appointments 3 days from now
2. Creates 3-day reminder notifications
3. Queries appointments 1 day from now
4. Creates 1-day reminder notifications
5. Only processes CONFIRMED appointments

## Database Schema

A new table `notifications` will be created with:

- id (Primary Key)
- user_id (Foreign Key to User)
- message (Text)
- type (Enum)
- is_read (Boolean)
- created_at (Timestamp)
- appointment_id (Foreign Key, nullable)
- prescription_id (Foreign Key, nullable)

## Testing

To test the notification system:

1. **Test Prescription Notification:**

   ```
   POST /api/prescriptions/{doctorId}/{patientId}
   Body: { "medicineName": "Aspirin", "dosage": "100mg", "instructions": "Take daily" }
   Then: GET /api/notifications/user/{patientId}
   ```

2. **Test Appointment Acceptance:**

   ```
   POST /api/appointments
   Body: { "date": "2025-12-30", "time": "10:00", "status": "CONFIRMED", ... }
   Then: GET /api/notifications/user/{clientId}
   ```

3. **Test Reminders (Manual Trigger):**

   - Create appointments 3 days and 1 day in the future
   - Wait for 9 AM or manually trigger the scheduler

4. **Test Notification Features:**
   ```
   GET /api/notifications/user/{userId}/unread/count
   PUT /api/notifications/{id}/read
   PUT /api/notifications/user/{userId}/read-all
   DELETE /api/notifications/{id}
   ```

## Frontend Integration Suggestions

1. **Notification Bell Icon:**

   - Display unread count badge
   - Fetch: GET /api/notifications/user/{userId}/unread/count

2. **Notification Dropdown:**

   - Show recent notifications
   - Fetch: GET /api/notifications/user/{userId}/unread

3. **Mark as Read:**

   - On click: PUT /api/notifications/{id}/read

4. **View All Notifications:**

   - Fetch: GET /api/notifications/user/{userId}

5. **Real-time Updates (Optional):**
   - Use WebSocket for instant notification delivery
   - Poll endpoint every 30-60 seconds

## Future Enhancements

1. Email notifications
2. SMS notifications
3. Push notifications (mobile app)
4. WebSocket real-time notifications
5. Notification preferences/settings
6. Custom reminder times
7. Notification templates
8. Multi-language support

## Notes

- All notification creation is wrapped in try-catch blocks to prevent failures from affecting core operations
- Scheduler uses SLF4J logging for monitoring
- Notifications are linked to their source entities (appointment/prescription) via IDs
- System is designed to be non-intrusive - notification failures won't break main functionality
