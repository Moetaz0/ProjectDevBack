package com.cc.project.Controllers;

import com.cc.project.Entity.ContactMessage;
import com.cc.project.Entity.User;
import com.cc.project.Service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

    private final ContactService contactService;

    // Fetch recipients by type
    @GetMapping("/recipients")
    public List<User> getRecipients(@RequestParam String type) {
        return contactService.getRecipientsByType(type);
    }

    // Fetch contact history
    @GetMapping("/contact-history")
    public List<ContactMessage> getHistory() {
        return contactService.getContactHistory();
    }

    // Send a contact message
    @PostMapping("/send-contact")
    public ContactMessage sendMessage(@RequestBody Map<String, Object> payload) {
        String recipientType = payload.get("recipientType").toString();
        Long recipientId = Long.parseLong(payload.get("recipientId").toString());
        Long senderId = Long.parseLong(payload.get("senderId").toString());
        String content = payload.get("content").toString();
        return contactService.sendContactMessage(recipientType, recipientId, senderId, content);
    }
}
