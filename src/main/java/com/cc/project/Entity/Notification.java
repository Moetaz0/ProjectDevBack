package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Reference IDs for linking to specific entities
    private Long appointmentId;
    private Long prescriptionId;

    public enum NotificationType {
        PRESCRIPTION_ADDED,
        APPOINTMENT_ACCEPTED,
        APPOINTMENT_REQUESTED,
        APPOINTMENT_REMINDER_3_DAYS,
        APPOINTMENT_REMINDER_1_DAY
    }
}
