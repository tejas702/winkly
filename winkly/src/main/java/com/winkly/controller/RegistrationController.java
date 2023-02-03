package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.UserRegisterRequestDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @PostMapping("/register_user")
    @ApiOperation("Register User")
    public ResponseEntity registerUser(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        if (userRepository.existsByUserName(userRegisterRequestDto.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageInfoDto("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userRegisterRequestDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageInfoDto("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity(userRegisterRequestDto.getUserName(),
                userRegisterRequestDto.getEmail(),
                encoder.encode(userRegisterRequestDto.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageInfoDto("User registered successfully!"));
    }
}
