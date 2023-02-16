package com.winkly.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping(value = "/winkly_sitemap")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Sitemap Controller")
public class SitemapController {
    @GetMapping(path = "/sitemap", produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation("Sitemap api")
    public ResponseEntity getSitemap() throws IOException {
        String filePath = "winkly/src/main/resources/assets/sitemap.xml";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line.trim());
        }

        String xml = stringBuilder.toString();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml);
    }
}
