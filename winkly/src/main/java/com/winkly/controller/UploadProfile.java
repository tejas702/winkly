package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.UpdateUserDetailsDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import com.winkly.service.impl.CloudinaryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/winkly_verify")
@Api(tags = "Upload Profile")
public class UploadProfile {

    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @PostMapping(value = "/upload", consumes = {})
    public ResponseEntity upload(@Valid @RequestHeader(value = "Authorization") String authToken, @RequestParam("file")
        MultipartFile file) throws IOException {
        authToken = authToken.replace("Bearer ", "");
        if (jwtUtils.checkExpiryForAccessToken(authToken)) {
            return ResponseEntity.badRequest().body(new MessageInfoDto("Token Expired"));
        }
        String extension = file.getOriginalFilename();
        if (extension.contains(".png")
            || extension.contains(".jpg")
            || extension.contains(".jpeg")
            || extension.contains(".gif")
            || extension.contains(".apng")
            || extension.contains(".avif")
            || extension.contains(".jfif")
            || extension.contains(".pjpeg")
            || extension.contains(".pjp")
            || extension.contains(".svg")
            || extension.contains(".webp")) {
          authToken = authToken.replace("Bearer ", "");
          String response = cloudinaryService.upload(authToken, file);
            if (response.equals("Successfully Uploaded")) {
                String email = jwtUtils.getEmailFromJwtToken(authToken);
                userRepository.updateVerifiedStatusEmail(email, "Pending");
                return ResponseEntity.ok().body(new MessageInfoDto(response));
            }
          return ResponseEntity.badRequest().body(new MessageInfoDto(response));
        }
        return ResponseEntity.badRequest().body(new MessageInfoDto("Invalid Image"));
    }
}
