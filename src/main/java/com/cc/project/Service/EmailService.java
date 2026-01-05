package com.cc.project.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Send verification code email with HTML template
     */
    public void sendVerificationCodeEmail(String toEmail, String code, String recipientName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MedLink - Verification Code");
            helper.setText(getVerificationCodeTemplate(code, recipientName), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    /**
     * Send password reset email with HTML template
     */
    public void sendPasswordResetEmail(String toEmail, String code, String recipientName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MedLink - Password Reset Code");
            helper.setText(getPasswordResetTemplate(code, recipientName), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset email", e);
        }
    }

    /**
     * Send welcome email after successful registration
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Welcome to MedLink");
            helper.setText(getWelcomeTemplate(username), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    /**
     * HTML Template for Verification Code
     */
    private String getVerificationCodeTemplate(String code, String recipientName) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MedLink - Verification Code</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background-color: #f5f5f5;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            color: white;\n" +
                "            padding: 30px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 28px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px;\n" +
                "        }\n" +
                "        .greeting {\n" +
                "            font-size: 16px;\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 14px;\n" +
                "            color: #666;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 25px;\n" +
                "        }\n" +
                "        .code-box {\n" +
                "            background-color: #f8f9fa;\n" +
                "            border: 2px dashed #667eea;\n" +
                "            border-radius: 8px;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            margin: 25px 0;\n" +
                "        }\n" +
                "        .code {\n" +
                "            font-size: 36px;\n" +
                "            font-weight: bold;\n" +
                "            color: #667eea;\n" +
                "            letter-spacing: 8px;\n" +
                "            font-family: 'Courier New', monospace;\n" +
                "        }\n" +
                "        .expiry {\n" +
                "            font-size: 12px;\n" +
                "            color: #e74c3c;\n" +
                "            text-align: center;\n" +
                "            margin-top: 15px;\n" +
                "        }\n" +
                "        .warning {\n" +
                "            background-color: #fff3cd;\n" +
                "            border-left: 4px solid #ffc107;\n" +
                "            padding: 15px;\n" +
                "            margin: 20px 0;\n" +
                "            border-radius: 4px;\n" +
                "            font-size: 13px;\n" +
                "            color: #856404;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f8f9fa;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            font-size: 12px;\n" +
                "            color: #999;\n" +
                "            border-top: 1px solid #eee;\n" +
                "        }\n" +
                "        .footer-text {\n" +
                "            margin: 5px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>🏥 MedLink</h1>\n" +
                "            <p style=\"margin: 10px 0 0 0; font-size: 14px; opacity: 0.9;\">Your Healthcare Partner</p>\n"
                +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Hello "
                + (recipientName != null && !recipientName.isEmpty() ? recipientName : "User") + ",</div>\n" +
                "            <div class=\"message\">\n" +
                "                Your verification code for MedLink is ready. Please use the code below to verify your email address.\n"
                +
                "            </div>\n" +
                "            <div class=\"code-box\">\n" +
                "                <div class=\"code\">" + code + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"expiry\">⏱️ This code is valid for 10 minutes</div>\n" +
                "            <div class=\"warning\">\n" +
                "                <strong>⚠️ Security Notice:</strong> Never share this code with anyone. MedLink staff will never ask for your verification code.\n"
                +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                If you did not request this code, please ignore this email. Your account remains secure.\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * HTML Template for Password Reset
     */
    private String getPasswordResetTemplate(String code, String recipientName) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MedLink - Password Reset</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background-color: #f5f5f5;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);\n" +
                "            color: white;\n" +
                "            padding: 30px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 28px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px;\n" +
                "        }\n" +
                "        .greeting {\n" +
                "            font-size: 16px;\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 14px;\n" +
                "            color: #666;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 25px;\n" +
                "        }\n" +
                "        .code-box {\n" +
                "            background-color: #f8f9fa;\n" +
                "            border: 2px dashed #e74c3c;\n" +
                "            border-radius: 8px;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            margin: 25px 0;\n" +
                "        }\n" +
                "        .code {\n" +
                "            font-size: 36px;\n" +
                "            font-weight: bold;\n" +
                "            color: #e74c3c;\n" +
                "            letter-spacing: 8px;\n" +
                "            font-family: 'Courier New', monospace;\n" +
                "        }\n" +
                "        .expiry {\n" +
                "            font-size: 12px;\n" +
                "            color: #e74c3c;\n" +
                "            text-align: center;\n" +
                "            margin-top: 15px;\n" +
                "        }\n" +
                "        .warning {\n" +
                "            background-color: #f8d7da;\n" +
                "            border-left: 4px solid #f5c6cb;\n" +
                "            padding: 15px;\n" +
                "            margin: 20px 0;\n" +
                "            border-radius: 4px;\n" +
                "            font-size: 13px;\n" +
                "            color: #721c24;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f8f9fa;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            font-size: 12px;\n" +
                "            color: #999;\n" +
                "            border-top: 1px solid #eee;\n" +
                "        }\n" +
                "        .footer-text {\n" +
                "            margin: 5px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>🔐 Password Reset</h1>\n" +
                "            <p style=\"margin: 10px 0 0 0; font-size: 14px; opacity: 0.9;\">MedLink Account Security</p>\n"
                +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Hello "
                + (recipientName != null && !recipientName.isEmpty() ? recipientName : "User") + ",</div>\n" +
                "            <div class=\"message\">\n" +
                "                We received a request to reset the password for your MedLink account. Use the code below to proceed with resetting your password.\n"
                +
                "            </div>\n" +
                "            <div class=\"code-box\">\n" +
                "                <div class=\"code\">" + code + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"expiry\">⏱️ This code is valid for 15 minutes</div>\n" +
                "            <div class=\"warning\">\n" +
                "                <strong>⚠️ Security Alert:</strong> If you did not request a password reset, please change your password immediately or contact support. Do not share this code with anyone.\n"
                +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                <strong>Steps to reset your password:</strong><br>\n" +
                "                1. Go to the password reset page<br>\n" +
                "                2. Enter your email address<br>\n" +
                "                3. Enter the code above<br>\n" +
                "                4. Create a new secure password<br>\n" +
                "                5. Confirm the new password\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * HTML Template for Welcome Email
     */
    private String getWelcomeTemplate(String username) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Welcome to MedLink</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background-color: #f5f5f5;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background: linear-gradient(135deg, #27ae60 0%, #229954 100%);\n" +
                "            color: white;\n" +
                "            padding: 30px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 28px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px;\n" +
                "        }\n" +
                "        .greeting {\n" +
                "            font-size: 20px;\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 14px;\n" +
                "            color: #666;\n" +
                "            line-height: 1.8;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .features {\n" +
                "            background-color: #f8f9fa;\n" +
                "            border-radius: 8px;\n" +
                "            padding: 20px;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .feature-item {\n" +
                "            display: flex;\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "        .feature-icon {\n" +
                "            font-size: 20px;\n" +
                "            margin-right: 15px;\n" +
                "            min-width: 25px;\n" +
                "        }\n" +
                "        .feature-text {\n" +
                "            font-size: 13px;\n" +
                "            color: #666;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f8f9fa;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            font-size: 12px;\n" +
                "            color: #999;\n" +
                "            border-top: 1px solid #eee;\n" +
                "        }\n" +
                "        .footer-text {\n" +
                "            margin: 5px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>🏥 Welcome to MedLink</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Welcome, "
                + (username != null && !username.isEmpty() ? username : "User") + "! 👋</div>\n" +
                "            <div class=\"message\">\n" +
                "                Thank you for joining MedLink! We're thrilled to have you on board. Your account has been successfully created and is ready to use.\n"
                +
                "            </div>\n" +
                "            <div class=\"features\">\n" +
                "                <strong style=\"color: #333; font-size: 14px;\">What you can do with MedLink:</strong>\n"
                +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">📅</div>\n" +
                "                    <div class=\"feature-text\"><strong>Book Appointments</strong> - Schedule appointments with healthcare professionals</div>\n"
                +
                "                </div>\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">💊</div>\n" +
                "                    <div class=\"feature-text\"><strong>Manage Prescriptions</strong> - View and manage your prescriptions</div>\n"
                +
                "                </div>\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">📋</div>\n" +
                "                    <div class=\"feature-text\"><strong>Medical History</strong> - Keep track of your medical records</div>\n"
                +
                "                </div>\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">🔒</div>\n" +
                "                    <div class=\"feature-text\"><strong>Secure & Private</strong> - Your health information is safe with us</div>\n"
                +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                If you have any questions or need assistance, our support team is here to help. Happy to serve you!\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * Send prescription notification email
     */
    public void sendPrescriptionNotificationEmail(String toEmail, String patientName, String medicineName,
            Long prescriptionId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MedLink - New Prescription Added");
            helper.setText(getPrescriptionNotificationTemplate(patientName, medicineName, prescriptionId), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send prescription notification email", e);
        }
    }

    /**
     * Send appointment confirmed email
     */
    public void sendAppointmentConfirmedEmail(String toEmail, String patientName, String appointmentDate,
            Long appointmentId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MedLink - Appointment Confirmed");
            helper.setText(getAppointmentConfirmedTemplate(patientName, appointmentDate, appointmentId), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send appointment confirmation email", e);
        }
    }

    /**
     * Send appointment reminder email
     */
    public void sendAppointmentReminderEmail(String toEmail, String patientName, String appointmentDate,
            Long appointmentId, String timeframe) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MedLink - Appointment Reminder");
            helper.setText(getAppointmentReminderTemplate(patientName, appointmentDate, appointmentId, timeframe),
                    true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send appointment reminder email", e);
        }
    }

    /**
     * Send appointment request email to doctor
     */
    public void sendAppointmentRequestEmail(String toEmail, String doctorName, String patientName,
            String appointmentDate, Long appointmentId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MedLink - New Appointment Request");
            helper.setText(getAppointmentRequestTemplate(doctorName, patientName, appointmentDate, appointmentId),
                    true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send appointment request email", e);
        }
    }

    /**
     * HTML Template for Prescription Notification
     */
    private String getPrescriptionNotificationTemplate(String patientName, String medicineName, Long prescriptionId) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MedLink - New Prescription</title>\n" +
                "    <style>\n" +
                "        body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }\n"
                +
                "        .container { max-width: 600px; margin: 40px auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }\n"
                +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 30px; text-align: center; }\n"
                +
                "        .header h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; }\n" +
                "        .content { padding: 40px 30px; }\n" +
                "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }\n" +
                "        .message { font-size: 16px; color: #555; line-height: 1.6; margin-bottom: 30px; }\n" +
                "        .info-box { background: #f8f9ff; border-left: 4px solid #667eea; padding: 20px; margin: 20px 0; border-radius: 8px; }\n"
                +
                "        .info-label { font-weight: 600; color: #667eea; margin-bottom: 5px; }\n" +
                "        .info-value { font-size: 18px; color: #333; font-weight: 500; }\n" +
                "        .button { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; margin: 20px 0; }\n"
                +
                "        .footer { background: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #e9ecef; }\n"
                +
                "        .footer-text { color: #6c757d; font-size: 12px; margin: 5px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>💊 New Prescription Added</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Hello " + patientName + ",</div>\n" +
                "            <div class=\"message\">\n" +
                "                Your doctor has added a new prescription to your medical file. Please review the details below:\n"
                +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Medicine</div>\n" +
                "                <div class=\"info-value\">" + medicineName + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Prescription ID</div>\n" +
                "                <div class=\"info-value\">#" + prescriptionId + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                Please log in to your MedLink account to view complete prescription details, dosage instructions, and refill information.\n"
                +
                "            </div>\n" +
                "            <div style=\"text-align: center;\">\n" +
                "                <a href=\"#\" class=\"button\">View Prescription Details</a>\n" +
                "            </div>\n" +
                "            <div class=\"message\" style=\"margin-top: 30px; font-size: 14px; color: #888;\">\n" +
                "                ⚠️ Important: Follow your doctor's instructions carefully. Contact your healthcare provider if you have any questions or concerns.\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * HTML Template for Appointment Confirmed
     */
    private String getAppointmentConfirmedTemplate(String patientName, String appointmentDate, Long appointmentId) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MedLink - Appointment Confirmed</title>\n" +
                "    <style>\n" +
                "        body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #27ae60 0%, #229954 100%); }\n"
                +
                "        .container { max-width: 600px; margin: 40px auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }\n"
                +
                "        .header { background: linear-gradient(135deg, #27ae60 0%, #229954 100%); padding: 40px 30px; text-align: center; }\n"
                +
                "        .header h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; }\n" +
                "        .content { padding: 40px 30px; }\n" +
                "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }\n" +
                "        .message { font-size: 16px; color: #555; line-height: 1.6; margin-bottom: 30px; }\n" +
                "        .info-box { background: #eafaf1; border-left: 4px solid #27ae60; padding: 20px; margin: 20px 0; border-radius: 8px; }\n"
                +
                "        .info-label { font-weight: 600; color: #27ae60; margin-bottom: 5px; }\n" +
                "        .info-value { font-size: 18px; color: #333; font-weight: 500; }\n" +
                "        .button { display: inline-block; background: linear-gradient(135deg, #27ae60 0%, #229954 100%); color: white; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; margin: 20px 0; }\n"
                +
                "        .footer { background: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #e9ecef; }\n"
                +
                "        .footer-text { color: #6c757d; font-size: 12px; margin: 5px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>✅ Appointment Confirmed</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Hello " + patientName + ",</div>\n" +
                "            <div class=\"message\">\n" +
                "                Great news! Your appointment has been confirmed by your healthcare provider.\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Appointment Date</div>\n" +
                "                <div class=\"info-value\">" + appointmentDate + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Appointment ID</div>\n" +
                "                <div class=\"info-value\">#" + appointmentId + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                You will receive reminder emails 3 days before and 1 day before your scheduled appointment. Please arrive 15 minutes early.\n"
                +
                "            </div>\n" +
                "            <div style=\"text-align: center;\">\n" +
                "                <a href=\"#\" class=\"button\">View Appointment Details</a>\n" +
                "            </div>\n" +
                "            <div class=\"message\" style=\"margin-top: 30px; font-size: 14px; color: #888;\">\n" +
                "                ℹ️ If you need to reschedule or cancel, please contact us at least 24 hours in advance.\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * HTML Template for Appointment Reminder
     */
    private String getAppointmentReminderTemplate(String patientName, String appointmentDate, Long appointmentId,
            String timeframe) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MedLink - Appointment Reminder</title>\n" +
                "    <style>\n" +
                "        body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%); }\n"
                +
                "        .container { max-width: 600px; margin: 40px auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }\n"
                +
                "        .header { background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%); padding: 40px 30px; text-align: center; }\n"
                +
                "        .header h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; }\n" +
                "        .content { padding: 40px 30px; }\n" +
                "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }\n" +
                "        .message { font-size: 16px; color: #555; line-height: 1.6; margin-bottom: 30px; }\n" +
                "        .info-box { background: #fef5e7; border-left: 4px solid #f39c12; padding: 20px; margin: 20px 0; border-radius: 8px; }\n"
                +
                "        .info-label { font-weight: 600; color: #f39c12; margin-bottom: 5px; }\n" +
                "        .info-value { font-size: 18px; color: #333; font-weight: 500; }\n" +
                "        .reminder-badge { background: #f39c12; color: white; padding: 8px 16px; border-radius: 20px; display: inline-block; font-weight: 600; margin: 10px 0; }\n"
                +
                "        .button { display: inline-block; background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%); color: white; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; margin: 20px 0; }\n"
                +
                "        .footer { background: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #e9ecef; }\n"
                +
                "        .footer-text { color: #6c757d; font-size: 12px; margin: 5px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>🔔 Appointment Reminder</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Hello " + patientName + ",</div>\n" +
                "            <div style=\"text-align: center;\">\n" +
                "                <span class=\"reminder-badge\">Upcoming " + timeframe + "</span>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                This is a friendly reminder about your upcoming appointment.\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Appointment Date</div>\n" +
                "                <div class=\"info-value\">" + appointmentDate + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Appointment ID</div>\n" +
                "                <div class=\"info-value\">#" + appointmentId + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                Please remember to:\n" +
                "                <ul style=\"color: #555; line-height: 1.8;\">\n" +
                "                    <li>Arrive 15 minutes early</li>\n" +
                "                    <li>Bring your medical insurance card</li>\n" +
                "                    <li>Bring any relevant medical records</li>\n" +
                "                    <li>Prepare any questions for your doctor</li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "            <div style=\"text-align: center;\">\n" +
                "                <a href=\"#\" class=\"button\">View Full Details</a>\n" +
                "            </div>\n" +
                "            <div class=\"message\" style=\"margin-top: 30px; font-size: 14px; color: #888;\">\n" +
                "                ⚠️ Need to reschedule? Contact us at least 24 hours in advance to avoid cancellation fees.\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * HTML Template for Appointment Request (sent to doctor)
     */
    private String getAppointmentRequestTemplate(String doctorName, String patientName, String appointmentDate,
            Long appointmentId) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MedLink - New Appointment Request</title>\n" +
                "    <style>\n" +
                "        body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); }\n"
                +
                "        .container { max-width: 600px; margin: 40px auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }\n"
                +
                "        .header { background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); padding: 40px 30px; text-align: center; }\n"
                +
                "        .header h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; }\n" +
                "        .content { padding: 40px 30px; }\n" +
                "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }\n" +
                "        .message { font-size: 16px; color: #555; line-height: 1.6; margin-bottom: 30px; }\n" +
                "        .info-box { background: #ebf5fb; border-left: 4px solid #3498db; padding: 20px; margin: 20px 0; border-radius: 8px; }\n"
                +
                "        .info-label { font-weight: 600; color: #3498db; margin-bottom: 5px; }\n" +
                "        .info-value { font-size: 18px; color: #333; font-weight: 500; }\n" +
                "        .status-badge { background: #3498db; color: white; padding: 8px 16px; border-radius: 20px; display: inline-block; font-weight: 600; margin: 10px 0; }\n"
                +
                "        .button { display: inline-block; background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); color: white; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; margin: 20px 0; }\n"
                +
                "        .button-secondary { display: inline-block; background: #95a5a6; color: white; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; margin: 20px 10px; }\n"
                +
                "        .footer { background: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #e9ecef; }\n"
                +
                "        .footer-text { color: #6c757d; font-size: 12px; margin: 5px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>📅 New Appointment Request</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">Hello Dr. " + doctorName + ",</div>\n" +
                "            <div style=\"text-align: center;\">\n" +
                "                <span class=\"status-badge\">PENDING APPROVAL</span>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                You have received a new appointment request from a patient. Please review the details below and take action.\n"
                +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Patient Name</div>\n" +
                "                <div class=\"info-value\">" + patientName + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Requested Date & Time</div>\n" +
                "                <div class=\"info-value\">" + appointmentDate + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"info-box\">\n" +
                "                <div class=\"info-label\">Appointment ID</div>\n" +
                "                <div class=\"info-value\">#" + appointmentId + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"message\">\n" +
                "                Please log in to your MedLink account to view full patient details, medical history, and appointment notes.\n"
                +
                "            </div>\n" +
                "            <div style=\"text-align: center;\">\n" +
                "                <a href=\"#\" class=\"button\">Approve Appointment</a>\n" +
                "                <a href=\"#\" class=\"button-secondary\">View Details</a>\n" +
                "            </div>\n" +
                "            <div class=\"message\" style=\"margin-top: 30px; font-size: 14px; color: #888;\">\n" +
                "                ℹ️ Note: Please respond to appointment requests within 24 hours to maintain good patient communication.\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <div class=\"footer-text\">© 2025 MedLink. All rights reserved.</div>\n" +
                "            <div class=\"footer-text\">This is an automated message. Please do not reply to this email.</div>\n"
                +
                "            <div class=\"footer-text\">For support, contact: support@medlink.com</div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
