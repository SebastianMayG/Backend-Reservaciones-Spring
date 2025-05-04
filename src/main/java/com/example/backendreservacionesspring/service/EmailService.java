package com.example.backendreservacionesspring.service;

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
//        context.setVariable("name", name);
//        context.setVariable("verificationLink", verificationLink);

        String htmlContent = templateEngine.process("verification", context);
        sendEmail(toEmail, "Verifica tu reservación", htmlContent);
    }

    /*public void sendReservationReminder(String toEmail, ReservationDetailsDto reservationDetails) {
        Context context = new Context();
        context.setVariable("reservation", reservationDetails);

        String htmlContent = templateEngine.process("reservation_reminder", context);
        sendEmail(toEmail, "Recordatorio de tu reservación", htmlContent);
    }*/

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