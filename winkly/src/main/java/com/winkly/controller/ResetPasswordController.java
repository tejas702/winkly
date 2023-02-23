package com.winkly.controller;

import com.winkly.entity.UserEntity;
import com.winkly.service.impl.UserDetailsServiceImpl;
import com.winkly.utils.Utility;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping(value = "")
@Api(tags = "Reset Password Controller")
public class ResetPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userDetailsService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (Exception e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link) {
        MimeMessage message = mailSender.createMimeMessage();
        SimpleMailMessage msg = new SimpleMailMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            msg.setFrom("winklyteam@gmail.com");
            helper.setFrom("winklyteam@gmail.com", "Winklyy Support");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            msg.setTo(recipientEmail);
            helper.setTo(recipientEmail);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        String subject = "Here's the link to reset your password";


        String content =
                "<p>Hello,</p>"
                        + "<p>You have requested to reset your password.</p>"
                        + "<p>Click the link below to change your password:</p>"
                        + "<p><a href=\""
                        + link
                        + "\">Change my password</a></p>"
                        + "<br>"
                        + "<p>Ignore this email if you do remember your password, "
                        + "or you have not made the request.</p>";

        try {
            msg.setSubject(subject);
            helper.setSubject(subject);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }

        try {
            msg.setText(content);
            helper.setText(content, true);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }

        mailSender.send(msg);
        log.info("{}", "message sent");
        mailSender.send(message);
        log.info("{}", "message sent");
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
        UserEntity customer = userDetailsService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        UserEntity customer = userDetailsService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userDetailsService.updatePassword(customer, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";

    }
}
