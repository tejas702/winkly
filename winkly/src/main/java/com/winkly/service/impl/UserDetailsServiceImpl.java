package com.winkly.service.impl;

import com.winkly.dto.ResetPasswordDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.utils.GenerateResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GenerateResetToken generateToken;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
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
