# Notification System API Testing Guide

## Quick Start Testing

### 1. Get User Notifications

```http
GET http://localhost:8080/api/notifications/user/1
```

### 2. Get Unread Notifications

```http
GET http://localhost:8080/api/notifications/user/1/unread
```

### 3. Get Unread Count

```http
GET http://localhost:8080/api/notifications/user/1/unread/count
```

### 4. Mark Notification as Read

```http
PUT http://localhost:8080/api/notifications/5/read
```

### 5. Mark All as Read

```http
PUT http://localhost:8080/api/notifications/user/1/read-all
```

### 6. Delete Notification

```http
DELETE http://localhost:8080/api/notifications/3
```

## Testing Notification Creation

### Test Prescription Notification

1. Create a prescription (will auto-create notification):

```http
POST http://localhost:8080/api/prescriptions/{doctorId}/{patientId}
Content-Type: application/json

{
  "medicineName": "Aspirin",
  "dosage": "100mg",
  "instructions": "Take once daily with food"
}
```

2. Check patient's notifications:

```http
GET http://localhost:8080/api/notifications/user/{patientId}
```

### Test Appointment Accepted Notification

1. Create/Update an appointment with CONFIRMED status:

```http
POST http://localhost:8080/api/appointments
Content-Type: application/json

{
  "date": "2025-12-30",
  "time": "10:00",
  "status": "CONFIRMED",
  "notes": "Regular checkup",
  "client": { "id": 1 },
  "doctor": { "id": 2 }
}
```

2. Check client's notifications:

```http
GET http://localhost:8080/api/notifications/user/1
```

### Test Appointment Reminders (Scheduler)

#### Setup Test Data:

1. Create appointments 3 days in the future:

```http
POST http://localhost:8080/api/appointments
Content-Type: application/json

{
  "date": "2025-12-27",  // 3 days from now (adjust based on current date)
  "time": "14:00",
  "status": "CONFIRMED",
  "client": { "id": 1 },
  "doctor": { "id": 2 }
}
```

2. Create appointments 1 day in the future:

```http
POST http://localhost:8080/api/appointments
Content-Type: application/json

{
  "date": "2025-12-25",  // 1 day from now (adjust based on current date)
  "time": "10:00",
  "status": "CONFIRMED",
  "client": { "id": 1 },
  "doctor": { "id": 2 }
}
```

#### Wait for Scheduler:

- The scheduler runs daily at 9:00 AM
- Check logs for: "Starting appointment reminder check..."
- Or manually trigger if you add a test endpoint

#### Verify Reminders:

```http
GET http://localhost:8080/api/notifications/user/1
```

You should see notifications with types:

- APPOINTMENT_REMINDER_3_DAYS
- APPOINTMENT_REMINDER_1_DAY

## Expected Response Formats

### Notification Object:

```json
{
  "id": 1,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com"
  },
  "message": "A new prescription for Aspirin has been added by your doctor.",
  "type": "PRESCRIPTION_ADDED",
  "isRead": false,
  "createdAt": "2025-12-24T10:30:00",
  "appointmentId": null,
  "prescriptionId": 5
}
```

### Notification List:

```json
[
  {
    "id": 3,
    "message": "Reminder: You have an appointment tomorrow on 2025-12-25 at 10:00.",
    "type": "APPOINTMENT_REMINDER_1_DAY",
    "isRead": false,
    "createdAt": "2025-12-24T09:00:00"
  },
  {
    "id": 2,
    "message": "Your appointment on 2025-12-30 at 14:00 has been confirmed.",
    "type": "APPOINTMENT_ACCEPTED",
    "isRead": true,
    "createdAt": "2025-12-23T15:20:00"
  }
]
```

### Unread Count:

```json
5
```

## Postman Collection (Import)

```json
{
  "info": {
    "name": "MedLink Notifications",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Get All Notifications",
      "request": {
        "method": "GET",
        "url": "{{baseUrl}}/api/notifications/user/{{userId}}"
      }
    },
    {
      "name": "Get Unread Notifications",
      "request": {
        "method": "GET",
        "url": "{{baseUrl}}/api/notifications/user/{{userId}}/unread"
      }
    },
    {
      "name": "Get Unread Count",
      "request": {
        "method": "GET",
        "url": "{{baseUrl}}/api/notifications/user/{{userId}}/unread/count"
      }
    },
    {
      "name": "Mark as Read",
      "request": {
        "method": "PUT",
        "url": "{{baseUrl}}/api/notifications/{{notificationId}}/read"
      }
    },
    {
      "name": "Mark All as Read",
      "request": {
        "method": "PUT",
        "url": "{{baseUrl}}/api/notifications/user/{{userId}}/read-all"
      }
    },
    {
      "name": "Delete Notification",
      "request": {
        "method": "DELETE",
        "url": "{{baseUrl}}/api/notifications/{{notificationId}}"
      }
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    },
    {
      "key": "userId",
      "value": "1"
    },
    {
      "key": "notificationId",
      "value": "1"
    }
  ]
}
```

## Troubleshooting

### No notifications appearing?

1. Check if the service methods are being called
2. Verify user IDs match
3. Check database for notifications table
4. Look for error messages in console

### Scheduler not running?

1. Verify `@EnableScheduling` is in ProjectApplication.java
2. Check application logs at 9:00 AM
3. Ensure Spring Boot is running continuously

### Notifications not saving?

1. Check database connection
2. Verify foreign key constraints
3. Check for exceptions in logs
4. Ensure User entity exists

## Database Queries (for debugging)

```sql
-- View all notifications
SELECT * FROM notifications ORDER BY created_at DESC;

-- Count unread by user
SELECT COUNT(*) FROM notifications WHERE user_id = 1 AND is_read = false;

-- View notifications by type
SELECT * FROM notifications WHERE type = 'PRESCRIPTION_ADDED';

-- Clear all notifications (testing)
DELETE FROM notifications;
```
