package com.winkly.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

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

    @GetMapping("/log_in")
    public ResponseEntity logInGoogle(@Autowired NetHttpTransport transport, @Autowired GsonFactory factory, HttpServletRequest
            request, @RequestParam(required = false) String providedToken) throws GeneralSecurityException, IOException, IllegalAccessException {

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
                        givenToken = verifier.verify(providedToken);
                    } catch (Exception ex) {
                        throw new IllegalAccessException("Invalid id_token");
                    }

                    String email = givenToken.getPayload().getEmail();
                    UserEntity user = new UserEntity(email);
                    userRepository.save(user);

                    return ResponseEntity.ok().body("Login Successful!");
                }

                String email = idToken.getPayload().getEmail();
                UserEntity user = new UserEntity(email);
                userRepository.save(user);

                return ResponseEntity.ok().body("Login Successful!");
            }
        }
        return ResponseEntity.ok().body("Login Unsuccessful!");
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
