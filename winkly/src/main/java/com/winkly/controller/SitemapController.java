package com.winkly.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/winkly_sitemap")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Sitemap Controller")
public class SitemapController {
    @GetMapping(path = "/sitemap", produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation("Sitemap api")
    public ResponseEntity getSitemap() throws IOException {

        File file = new ClassPathResource("assets/sitemap.xml").getFile();
        String content = new String(Files.readAllBytes(file.toPath()));

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(content);
    }
}
