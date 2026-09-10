package in.sd.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.from:${spring.mail.username:}}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendPasswordReset(String email, String name, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (from != null && !from.isBlank()) message.setFrom(from);
        message.setTo(email);
        message.setSubject("IT SUTRAA - Reset your password");
        message.setText("Hello " + (name == null || name.isBlank() ? "Student" : name) + ",\n\n"
                + "We received a request to reset your IT SUTRAA password.\n\n"
                + "Open this link to create a new password:\n" + resetUrl + "\n\n"
                + "This link is valid for 15 minutes and can be used only once.\n"
                + "If you did not request this, you can safely ignore this email.\n\n"
                + "Regards,\nIT SUTRAA Team");
        mailSender.send(message);
    }
}
