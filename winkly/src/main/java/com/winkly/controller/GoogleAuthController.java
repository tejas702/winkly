package com.winkly.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.winkly.config.JwtUtils;
import com.winkly.dto.JwtResponseDto;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.MultipleMessageDto;
import com.winkly.dto.TokenDto;
import com.winkly.entity.RefreshToken;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.service.impl.RefreshTokenServiceImpl;
import com.winkly.service.impl.UserDetailsImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/winkly_google")
@Api(tags = "Google Auth Controller")
public class GoogleAuthController {

    @Value("${com.google.clientid}")
    private String gClientId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/log_in")
    public ResponseEntity logInGoogle(@Autowired NetHttpTransport transport, @Autowired GsonFactory factory, HttpServletRequest
            request, @RequestBody(required = false) TokenDto providedToken) throws GeneralSecurityException, IOException, IllegalAccessException {

        String token = "";
        try {
            token = this.getTokenFromRequest(request);
        } catch (Exception e) {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, factory)
                    .setAudience(Collections.singletonList(gClientId))
                    .build();

            GoogleIdToken idToken = null;

            try {
                idToken = verifier.verify(token);
            } catch (Exception exp) {

                GoogleIdToken givenToken;

                if (idToken == null) {

                    try {
                        givenToken = verifier.verify(providedToken.getToken());
                    } catch (Exception ex) {
                        throw new IllegalAccessException("Invalid id_token");
                    }

                    String email = givenToken.getPayload().getEmail();
                    UserEntity user = new UserEntity(email);

                    if (!userRepository.existsByEmail(email)) userRepository.save(user);

                    UserDetailsImpl userDetails = new UserDetailsImpl(email);

                    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

                    Optional<UserEntity> userTemp = userRepository.findByEmail(userDetails.getEmail());
                    String username = userTemp.get().getUsername();

                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getEmail());

                    return ResponseEntity.ok().body(new MultipleMessageDto(new JwtResponseDto(jwtCookie.getValue(), refreshToken.getToken(), user.getId(),
                            username, userDetails.getEmail()), "Login Successful!"));
                }

                String email = idToken.getPayload().getEmail();
                UserEntity user = new UserEntity(email);
                if (!userRepository.existsByEmail(email))
                userRepository.save(user);

                UserDetailsImpl userDetails = new UserDetailsImpl(email);

                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

                Optional<UserEntity> userTemp = userRepository.findByEmail(userDetails.getEmail());
                String username = userTemp.get().getUsername();

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getEmail());

                return ResponseEntity.ok().body(new MultipleMessageDto(new JwtResponseDto(jwtCookie.getValue(), refreshToken.getToken(), user.getId(),
                        username, userDetails.getEmail()), "Login Successful!"));
            }
        }
        return ResponseEntity.ok().body(new MessageInfoDto("Login Unsuccessful!"));
    }

    public String getTokenFromRequest(HttpServletRequest request) throws IllegalAccessException {
        String token = request.getHeader("Authorization");
        String[] parts = token.split(" ");
        if (parts.length != 2 || !parts[0].contains("Bearer")) {
            throw new IllegalAccessException("Authorization Bearer format invalid. <Bearer {token}>");
        }
        return parts[1];
    }
}
