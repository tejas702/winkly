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
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/winkly_verify")
@Api(tags = "Upload Profile")
public class UploadProfile {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = {"*/*"})
    public ResponseEntity upload(@Valid @RequestHeader(value = "Authorization") String authToken, @RequestParam("file")
        MultipartFile file) throws IOException {
        String response = cloudinaryService.upload(authToken, file);
        if (response.equals("Successfully Uploaded"))
            return ResponseEntity.ok().body(new MessageInfoDto(response));
        return ResponseEntity.badRequest().body(new MessageInfoDto(response));

    }
}
