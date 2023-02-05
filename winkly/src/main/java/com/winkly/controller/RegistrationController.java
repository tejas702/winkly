package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.PasswordDto;
import com.winkly.dto.ResetPasswordDto;
import com.winkly.dto.UserRegisterRequestDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.service.impl.UserDetailsServiceImpl;
import com.winkly.utils.GenerateResetToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/winkly")
@Api(tags = "Registration Controller")
public class RegistrationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    private final JavaMailSender mailSender;

    private final GenerateResetToken generateResetToken;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register_user")
    @ApiOperation("Register User")
    public ResponseEntity registerUser(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        if (userRepository.existsByEmail(userRegisterRequestDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageInfoDto("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity(userRegisterRequestDto.getEmail(),
                encoder.encode(userRegisterRequestDto.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageInfoDto("User registered successfully!"));
    }

    @PostMapping("/forgot_password")
    @ApiOperation("Forgot Password")
    public void forgotPass(HttpServletRequest request, @Valid @RequestParam String email) {

        ResetPasswordDto response = userDetailsService.forgotPassword(email);

        if (!response.getToken().equals("null")) {
            mailSender.send(generateResetToken.constructResetTokenEmail(request.getContextPath(),
                    request.getLocale(), response.getToken(), email));
        }
    }

    @GetMapping("/change_password")
    public String  changePassword(Locale locale, Model model, @RequestParam("token") String token) {
        String result = userDetailsService.checkTokenValidity(token);
        if(result != null) {
            return "Bad Request";
        } else {
            model.addAttribute("token", token);
            return "redirect:/update_password.html?lang=" + locale.getLanguage();
        }
    }

    @PutMapping("/reset_password")
    @ApiOperation("Reset Password")
    public String resetPass(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        UserEntity user = userRepository.findByResetToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userRepository.updatePassword(
                    encoder.encode(password), user.getEmail());
            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }
}
