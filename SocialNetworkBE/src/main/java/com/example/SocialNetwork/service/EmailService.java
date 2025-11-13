package com.example.SocialNetwork.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender mailSender;
    TemplateEngine templateEngine;
    @NonFinal
    @Value("${spring.mail.fromName}")
    String FROM_NAME;

    @NonFinal
    @Value("${spring.mail.username}")
    String FROM_EMAIL;

    @NonFinal
    @Value("${spring.mail.booking.subject}")
    String SUBJECT;

    @NonFinal
    @Value("${frontend.url}")
    private String frontEndUrl;


    @Async
    public void sendHtmlEmail(String to, String fullName, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("fullName", fullName);
            variables.put("frontEndUrl", frontEndUrl + "/register/verify");
            variables.put("token", token);
            context.setVariables(variables);
            String htmlContent = templateEngine.process("email-confirmation", context);
            helper.setFrom(FROM_EMAIL, FROM_NAME);
            helper.setTo(to);
            helper.setSubject(SUBJECT);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        }
        catch (MessagingException | UnsupportedEncodingException e){
            System.out.println(e);
        }


    }
}
