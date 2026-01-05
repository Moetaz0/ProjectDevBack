package com.cc.project.Service;

import com.cc.project.Entity.ContactMessage;
import com.cc.project.Entity.User;
import com.cc.project.Repository.ContactMessageRepository;
import com.cc.project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public List<User> getRecipientsByType(String type) {
        User.Role role = mapRecipientType(type);
        return userRepository.findByRole(role);
    }

    public List<ContactMessage> getContactHistory() {
        return contactMessageRepository.findAllByOrderBySentAtDesc();
    }

    public ContactMessage sendContactMessage(String recipientType, Long recipientId, Long senderId, String content) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found with ID: " + recipientId +
                        ". Please ensure recipients are initialized in the database."));

        ContactMessage message = new ContactMessage();
        message.setRecipientType(recipientType);
        message.setRecipientId(recipientId);
        message.setRecipientEmail(recipient.getEmail());
        message.setSenderId(senderId);
        message.setRecipientName(recipient.getUsername());
        message.setContent(content);

        contactMessageRepository.save(message);

        // Send email
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipient.getEmail());
            email.setSubject("New message from Patient via Contact Form");
            email.setText(content);
            mailSender.send(email);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }

        return message;
    }

    private User.Role mapRecipientType(String recipientType) {
        String type = recipientType == null ? "" : recipientType.trim().toLowerCase();
        switch (type) {
            case "doctor":
                return User.Role.DOCTOR;
            case "lab":
            case "labs":
                return User.Role.Labs;
            case "hospital":
            case "hospitals":
                return User.Role.HOSPITALS;
            case "admin":
                return User.Role.ADMIN;
            case "client":
                return User.Role.CLIENT;
            default:
                throw new IllegalArgumentException("Unsupported recipient type: " + recipientType);
        }
    }
}
