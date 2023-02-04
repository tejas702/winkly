package com.winkly.utils;

import com.winkly.entity.UserEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Component
public class GenerateResetToken {

    public String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    public boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= 30;
    }

    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, String email) {

        String url = contextPath + "/winkly/change_passsword?token=" + token;
        String message = "Reset Password";
        return constructEmail("Reset Password", message + " \r\n" + url, email);
    }

    public SimpleMailMessage constructEmail(String subject, String body, String mail) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(mail);
        email.setFrom("winklyteam@gmail.com");
        return email;
    }
}
