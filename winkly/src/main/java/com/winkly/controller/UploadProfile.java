package com.winkly.controller;

import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.UpdateUserDetailsDto;
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
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/winkly_verify")
@Api(tags = "Upload Profile")
public class UploadProfile {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = {"*/*"})
    public ResponseEntity upload(@RequestHeader(value = "Authorization") String authToken, @RequestPart(name = "file")
        MultipartFile file, @RequestParam String username) throws IOException {
        try {
            String response = cloudinaryService.upload(authToken, username, file);
            if (response.equals("Upload image again"))
                return ResponseEntity.badRequest().body(new MessageInfoDto(response));
            return ResponseEntity.ok().body(new MessageInfoDto(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageInfoDto("Upload image again"));
        }
    }
}
