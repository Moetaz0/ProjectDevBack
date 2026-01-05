package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientType; // Doctor, Lab, Pharmacy
    private Long recipientId; // ID of recipient
    private Long senderId; // ID of sender (user)
    private String recipientEmail; // Optional, for sending email
    private String recipientName; // Optional, for display
    private String content;
    private LocalDateTime sentAt = LocalDateTime.now();
}
