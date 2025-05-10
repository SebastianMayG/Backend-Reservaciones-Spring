package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.model.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationEmail(String toEmail) {
        Context context = new Context();

        String htmlContent = templateEngine.process("verification", context);
        sendEmail(toEmail, "Verifica tu reservación", htmlContent);
    }

    public void sendReservationReminder(String toEmail, Reservation reservation, User user) {
        Context context = new Context();
        context.setVariable("reservation", reservation);
        context.setVariable("user", user);

        String htmlContent = templateEngine.process("reservation_reminder", context);
        sendEmail(toEmail, "Recordatorio: Tu reservación es mañana", htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom("al064489@uacam.mx"); // Añade esta línea
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}