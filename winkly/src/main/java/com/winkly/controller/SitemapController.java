package com.winkly.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@RestController
@RequestMapping(value = "/winkly_sitemap")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Sitemap Controller")
public class SitemapController {
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(path = "/sitemap", produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation("Sitemap api")
    public ResponseEntity getSitemap() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:sitemap.xml");
        Reader reader = new InputStreamReader(resource.getInputStream());
        String content =  FileCopyUtils.copyToString(reader);

        return ResponseEntity.ok().body(content);
    }
}
