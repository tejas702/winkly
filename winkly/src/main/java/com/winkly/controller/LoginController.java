package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.*;
import com.winkly.entity.RefreshToken;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.service.impl.RefreshTokenServiceImpl;
import com.winkly.service.impl.UserDetailsImpl;
import com.winkly.utils.TokenRefreshException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/winkly_session")
@Api(tags = "Login Controller")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/log_in")
    public ResponseEntity authenticateUser(@Valid @RequestBody UserLoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        Optional<UserEntity> user = userRepository.findByEmail(userDetails.getEmail());
        String username = user.get().getUsername();

        String verifiedStatus = user.get().getVerifiedStatus();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getEmail());

        return ResponseEntity.ok().body(new MultipleMessageDto(new JwtResponseDto(jwtCookie.getValue(),
                refreshToken.getToken(), userDetails.getId(),
                username, userDetails.getEmail()), "Login Successful!", verifiedStatus));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity refreshtoken(@Valid @RequestBody TokenRefreshRequestDto request) {
        String requestRefreshToken = request.getRefreshToken();
        return ResponseEntity.noContent().build();
    }
}
