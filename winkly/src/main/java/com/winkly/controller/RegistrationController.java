package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.JwtResponseDto;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.MultipleMessageDto;
import com.winkly.dto.UserRegisterRequestDto;
import com.winkly.entity.RefreshToken;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.service.impl.RefreshTokenServiceImpl;
import com.winkly.service.impl.UserDetailsImpl;
import com.winkly.service.impl.UserDetailsServiceImpl;
import com.winkly.utils.GenerateResetToken;
import com.winkly.utils.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@Slf4j
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

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

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

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userRegisterRequestDto.getEmail(),
                        userRegisterRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(jwtCookie.getValue());

        return ResponseEntity.ok().body(new MultipleMessageDto(new JwtResponseDto(jwtCookie.getValue(), refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail()), "User Registered Successfully", user.getVerifiedStatus()));
    }
}
