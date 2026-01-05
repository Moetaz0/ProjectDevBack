# Email Template System - Implementation Guide

## Overview

A professional email template system has been implemented for MedLink to send beautifully formatted HTML emails for:

- Verification codes (password reset)
- Welcome emails after registration
- Password reset codes

## Components Created

### EmailService.java

Located in: `src/main/java/com/cc/project/Service/EmailService.java`

#### Methods:

1. **sendVerificationCodeEmail(String toEmail, String code, String recipientName)**

   - Sends a verification code with HTML template
   - Used for email verification (can be extended)

2. **sendPasswordResetEmail(String toEmail, String code, String recipientName)**

   - Sends password reset code in a professional HTML template
   - Includes security warnings
   - Shows 15-minute expiry

3. **sendWelcomeEmail(String toEmail, String username)**
   - Sends welcome email to new users
   - Highlights key features of MedLink
   - Professional branding

## Email Templates

### 1. Verification Code Email

**Color Scheme:** Purple gradient (667eea - 764ba2)

**Features:**

- Clear code display with large, monospace font
- 10-minute expiry notice
- Security warnings
- Professional header with MedLink branding

**HTML Structure:**

- Responsive design (600px max width)
- Mobile-friendly
- UTF-8 encoding for special characters

### 2. Password Reset Email

**Color Scheme:** Red gradient (e74c3c - c0392b)

**Features:**

- Prominent security alert
- Step-by-step reset instructions
- 15-minute expiry notice
- Warning about unauthorized reset requests

**Content:**

```
Subject: MedLink - Password Reset Code
Contains:
- Greeting with username
- Code in large format
- 5-step instructions to reset password
- Security warning
- Footer with support info
```

### 3. Welcome Email

**Color Scheme:** Green gradient (27ae60 - 229954)

**Features:**

- Warm greeting
- Feature highlights with icons:
  - 📅 Book Appointments
  - 💊 Manage Prescriptions
  - 📋 Medical History
  - 🔒 Secure & Private
- Professional footer

## Integration with AuthController

### Changes Made:

1. **Removed:**

   - `SimpleMailMessage` import
   - `JavaMailSender` injection
   - Plain text email sending

2. **Added:**
   - `EmailService` injection
   - Template-based email sending
   - Try-catch blocks for error handling

### Email Sending Flow:

#### Registration (Signup)

```
1. User submits registration
2. User is saved to database
3. Welcome email is sent (non-blocking)
4. Response returned to user
```

#### Forgot Password

```
1. User requests password reset
2. 6-digit code is generated
3. Templated email is sent (non-blocking)
4. Code stored in memory
5. Response returned to user
```

## Email Template Features

### Responsive Design

- Works on desktop, tablet, and mobile
- Max width: 600px
- Inline CSS for compatibility

### Security Elements

- ⚠️ Security warnings for sensitive actions
- Clear expiry times
- Professional branding
- Contact information for support

### Professional Styling

- Gradient headers
- Color-coded messages (red for security, green for success, orange for warnings)
- Consistent fonts (Segoe UI)
- Clear visual hierarchy

### Accessibility

- UTF-8 encoding
- Semantic HTML
- High contrast ratios
- Clear font sizes

## Usage Examples

### In AuthController (Already Integrated):

```java
// Send welcome email after signup
try {
    emailService.sendWelcomeEmail(newUser.getEmail(), newUser.getUsername());
} catch (Exception e) {
    System.err.println("Failed to send welcome email: " + e.getMessage());
}

// Send password reset email
try {
    emailService.sendPasswordResetEmail(email, code, user.getUsername());
} catch (Exception e) {
    System.err.println("Failed to send password reset email: " + e.getMessage());
}
```

### Extending to Other Services:

To use email templates in other services:

```java
@Service
@RequiredArgsConstructor
public class YourService {
    private final EmailService emailService;

    public void notifyUser(User user, String code) {
        emailService.sendVerificationCodeEmail(
            user.getEmail(),
            code,
            user.getUsername()
        );
    }
}
```

## Email Configuration

### application.properties Settings:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Note:** The application already has these configured.

### Gmail Setup:

1. Enable 2-factor authentication on Gmail
2. Generate an App Password
3. Use the App Password (not your regular password) in the configuration

## Template Customization

### To Modify Templates:

1. Edit the private methods in `EmailService.java`:

   - `getVerificationCodeTemplate()`
   - `getPasswordResetTemplate()`
   - `getWelcomeTemplate()`

2. Update CSS styling within the HTML string
3. Modify text content and messages
4. Change colors by updating hex values:
   - Purple: `#667eea`
   - Red: `#e74c3c`
   - Green: `#27ae60`

### To Add New Templates:

```java
public void sendCustomEmail(String toEmail, String customContent) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Your Custom Subject");
        helper.setText(getCustomTemplate(customContent), true);

        mailSender.send(message);
    } catch (MessagingException e) {
        throw new RuntimeException("Failed to send email", e);
    }
}

private String getCustomTemplate(String content) {
    return "<!DOCTYPE html>...\n" + content + "\n</html>";
}
```

## Error Handling

All email sending is wrapped in try-catch blocks to prevent:

- Application crashes from email failures
- User experience disruption
- Lost registration/password reset requests

Errors are logged to console and error responses are returned to API callers.

## Testing Email Templates

### Manual Testing:

1. **Test Welcome Email:**

   ```
   POST /auth/signup
   {
     "username": "testuser",
     "email": "test@gmail.com",
     "password": "securepass123"
   }
   ```

   Check your test email inbox for the welcome message.

2. **Test Password Reset Email:**
   ```
   POST /auth/forgot-password
   {
     "email": "test@gmail.com"
   }
   ```
   Check your test email inbox for the reset code.

### Using Email Testing Services:

- **Mailtrap.io** - Free email testing
- **MailHog** - Local SMTP testing
- **Gmail** - Use a test account

## Performance Considerations

- Email sending is **synchronous** (blocks the request)
- For **large-scale production**, consider:
  - Making email sending asynchronous with `@Async`
  - Using message queues (RabbitMQ, Kafka)
  - Implementing email scheduling

### Example Async Implementation:

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.initialize();
        return executor;
    }
}

// In EmailService:
@Async
public void sendVerificationCodeEmailAsync(String toEmail, String code, String recipientName) {
    sendVerificationCodeEmail(toEmail, code, recipientName);
}
```

## Future Enhancements

1. **Email Scheduling:**

   - Schedule emails to send at specific times
   - Bulk email campaigns

2. **Email Analytics:**

   - Track email opens
   - Track link clicks
   - Delivery monitoring

3. **SMS Fallback:**

   - Send SMS if email fails
   - Multi-channel notifications

4. **Notification Preferences:**

   - Users choose email frequency
   - Unsubscribe management
   - Email digest options

5. **Dynamic Content:**

   - Personalized recommendations
   - User-specific information
   - Dynamic styling based on preferences

6. **Email Service Integration:**
   - SendGrid
   - AWS SES
   - Mailgun
   - SendInBlue

## Troubleshooting

### Email Not Sending?

1. **Check Gmail Configuration:**

   - Ensure 2FA is enabled
   - Generate new App Password
   - Update credentials in `application.properties`

2. **Check Firewall/Network:**

   - Port 587 must be open for SMTP
   - No network blocking SMTP traffic

3. **Enable Debug Logging:**

   ```properties
   logging.level.org.springframework.mail=DEBUG
   logging.level.org.springframework.mail.javamail=DEBUG
   ```

4. **Check Recipient Email:**
   - Ensure email is valid
   - Not in spam filters

### Common Errors:

**"Failed to send email: Authentication failed"**

- Gmail password is incorrect
- App Password not generated properly

**"Failed to send email: Timeout"**

- Network connectivity issue
- Firewall blocking SMTP

**"Message part 0 does not contain text/plain"**

- Character encoding issue
- Try setting UTF-8 explicitly (already done)

## Security Best Practices

✅ Implemented:

- HTML templates prevent code injection
- UTF-8 encoding prevents encoding attacks
- Error messages don't expose sensitive info
- Non-blocking error handling

🔒 Additional Recommendations:

- Store email addresses encrypted at rest
- Implement rate limiting on email endpoints
- Log email sending for audit trails
- Implement DKIM/SPF for domain verification
- Monitor email delivery rates

## Support

For issues or questions about the email system:

1. Check `application.properties` configuration
2. Review console logs for detailed error messages
3. Test with a simple email first
4. Verify Gmail account security settings
