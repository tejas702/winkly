package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.UpdateUserDetailsDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/winkly_update")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Update Socials Controller")
public class UserDetailsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PutMapping("/update_socials")
    @ApiOperation("Update User Social Details")
    public ResponseEntity updateUserDetails(@Valid @RequestBody UpdateUserDetailsDto updateUserDetailsDto, @RequestParam
        String token) {
            String fbLink = updateUserDetailsDto.getFbLink();
            String instaLink = updateUserDetailsDto.getInstaLink();
            String linktreeLink = updateUserDetailsDto.getLinktreeLink();
            String linkedinLink = updateUserDetailsDto.getLinkedinLink();
            String snapchatLink = updateUserDetailsDto.getSnapchatLink();
            String twitterLink = updateUserDetailsDto.getTwitterLink();
            String email = jwtUtils.getEmailFromJwtToken(token);
            String username = updateUserDetailsDto.getUsername();
            if (!userRepository.existsByUsername(username))
            userRepository.updateSocials(fbLink, snapchatLink, twitterLink, instaLink, linkedinLink, linktreeLink,
                    email, username);
            else {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            return ResponseEntity.ok().body("Socials Updated");
    }

    //public void updateLikes(@Valid @RequestParam String user, String userLiked) {}
}
