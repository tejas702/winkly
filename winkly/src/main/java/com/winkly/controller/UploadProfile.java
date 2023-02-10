package com.winkly.controller;

import com.winkly.dto.UpdateUserDetailsDto;
import com.winkly.service.impl.CloudinaryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/winkly_verify")
@Api(tags = "Upload Profile")
public class UploadProfile {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestHeader(value = "authToken") String authToken, @RequestBody UpdateUserDetailsDto
            updateUserDetailsDto, @RequestParam("file") MultipartFile file) {
        return cloudinaryService.upload(authToken, updateUserDetailsDto, file);
    }
}
