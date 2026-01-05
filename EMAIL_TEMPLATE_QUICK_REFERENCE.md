# Email Template System - Quick Reference

## What Was Added

### 1. EmailService.java

**File:** `src/main/java/com/cc/project/Service/EmailService.java`

**Three main methods:**

```java
// For verification/email confirmation
emailService.sendVerificationCodeEmail(String toEmail, String code, String recipientName)

// For password reset
emailService.sendPasswordResetEmail(String toEmail, String code, String recipientName)

// For new user welcome
emailService.sendWelcomeEmail(String toEmail, String username)
```

### 2. Updated AuthController.java

**File:** `src/main/java/com/cc/project/Controllers/AuthController.java`

**Changes:**

- ✅ Replaced `SimpleMailMessage` with `EmailService`
- ✅ Added welcome email on signup
- ✅ Updated password reset to use HTML template
- ✅ Added error handling for email failures

## Email Templates Included

### 📧 Verification Code Email

```
Subject: MedLink - Verification Code
Color: Purple
Expiry: 10 minutes
Features: Code display, security warning
```

### 🔐 Password Reset Email

```
Subject: MedLink - Password Reset Code
Color: Red
Expiry: 15 minutes
Features: Code display, 5-step instructions, security alert
```

### 👋 Welcome Email

```
Subject: Welcome to MedLink
Color: Green
Features: Feature highlights, professional greeting
```

## Email Styling Features

✨ **Professional Design:**

- Responsive HTML/CSS
- Works on mobile, tablet, desktop
- Modern gradient headers
- Clear visual hierarchy
- Security warnings (when needed)

🎨 **Customizable:**

- Change colors easily
- Modify text content
- Update logos/branding
- Adjust styling

🔒 **Secure:**

- Prevents code injection
- UTF-8 encoding
- No sensitive info in errors

## How It Works

### Registration Flow

```
1. User fills signup form
2. POST /auth/signup
3. User saved to database
4. Welcome email sent (non-blocking)
5. Response with token returned
```

### Password Reset Flow

```
1. User clicks "Forgot Password"
2. POST /auth/forgot-password
3. 6-digit code generated
4. HTML email sent
5. Code stored in server memory
6. User verifies code
7. POST /auth/reset-password
8. Password updated
```

## Testing

### Test Email Registration:

```bash
curl -X POST http://localhost:8000/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "yourtest@gmail.com",
    "password": "pass123"
  }'
```

### Test Password Reset:

```bash
curl -X POST http://localhost:8000/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "yourtest@gmail.com"
  }'
```

Check your email inbox for the templates!

## Configuration Required

Already configured in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=motezab13@gmail.com
spring.mail.password=gfpo cutc mgvx ccla
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## Customizing Email Templates

### Changing Text:

In `EmailService.java`, find the template method and modify the string:

```java
private String getVerificationCodeTemplate(String code, String recipientName) {
    return "<!DOCTYPE html>\n" +
        // Change the message here
        "<div class=\"message\">Your custom message</div>\n" +
        // ...
}
```

### Changing Colors:

Replace hex color codes:

```css
/* Purple */
#667eea → your-color-code

/* Red */
#e74c3c → your-color-code

/* Green */
#27ae60 → your-color-code
```

## Available for Other Services

To use in other services (e.g., NotificationService):

```java
@Service
@RequiredArgsConstructor
public class SomeService {
    private final EmailService emailService;

    public void doSomething() {
        // Send verification email
        emailService.sendVerificationCodeEmail("user@email.com", "123456", "John");

        // Or send custom notifications
        emailService.sendPasswordResetEmail("user@email.com", "654321", "John");
    }
}
```

## Email Status Codes

| Endpoint                   | Status | Meaning                          |
| -------------------------- | ------ | -------------------------------- |
| POST /auth/signup          | 200    | User created, welcome email sent |
| POST /auth/signup          | 400    | Validation error, no email sent  |
| POST /auth/signup          | 500    | Database/email error             |
| POST /auth/forgot-password | 200    | Reset code sent                  |
| POST /auth/forgot-password | 400    | User not found                   |
| POST /auth/forgot-password | 500    | Email sending failed             |

## Notes

⚠️ **Current Implementation:**

- Email sending is **synchronous** (request waits for email)
- Email failures are logged but don't break signup/reset flow
- Codes are stored in memory (ConcurrentHashMap)
- No persistence layer for codes

📝 **For Production:**

- Consider async email sending with `@Async`
- Store reset codes in database with expiry timestamps
- Implement rate limiting on /auth endpoints
- Add email delivery monitoring
- Use email service provider (SendGrid, AWS SES)

## Troubleshooting

### "Failed to send email" error?

1. Check Gmail password is correct
2. Verify App Password is generated
3. Check internet connection
4. Check firewall allows port 587

### Email not arriving?

1. Check recipient email is correct
2. Check spam/junk folder
3. Check email logs in console
4. Verify Gmail account security

### Want to use different email provider?

1. Create new EmailService methods
2. Implement MimeMessage sending
3. Update authentication credentials
4. Test thoroughly before deploying

---

**Need more details?** See `EMAIL_TEMPLATE_GUIDE.md` for comprehensive documentation.
