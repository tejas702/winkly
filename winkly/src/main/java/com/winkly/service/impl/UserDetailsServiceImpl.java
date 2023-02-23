package com.winkly.service.impl;

import com.cloudinary.api.exceptions.BadRequest;
import com.winkly.dto.ResetPasswordDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.utils.GenerateResetToken;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GenerateResetToken generateToken;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }

    public void updateResetPasswordToken(String token, String email) {
        Optional<UserEntity> customer = userRepository.findByEmail(email);
        UserEntity user = new UserEntity();
        if (Objects.nonNull(customer)) {
            customer.get().setResetToken(token);
            userRepository.save(customer.get());
        } else {
            throw new RuntimeException("Could not find any customer with the email " + email);
        }
    }

    public UserEntity getByResetPasswordToken(String token) {
        return userRepository.findByResetToken(token);
    }

    public void updatePassword(UserEntity customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);

        customer.setResetToken(null);
        userRepository.save(customer);
    }

    public ResetPasswordDto forgotPassword(String email) {

        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            return ResetPasswordDto.builder()
                    .token("null")
                    .build();
        }

        String token = generateToken.generateToken();
        resetPasswordDto.setEmail(email);
        resetPasswordDto.setTokenCreationTime(LocalDateTime.now());
        resetPasswordDto.setToken(token);

        return resetPasswordDto;
    }

    public String checkTokenValidity(String token) {

        UserEntity user = userRepository.findByResetToken(token);

        if(token.equals("null")) {
            return "null";
        }

        LocalDateTime tokenCreationDate = user.getTokenCreationTime();

        if (generateToken.isTokenExpired(tokenCreationDate)) {
          return "null";
        }

        return "Token Valid";
    }
}
