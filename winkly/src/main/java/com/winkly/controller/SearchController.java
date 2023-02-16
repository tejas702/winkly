package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.SearchDto;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/winkly_search")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Search Controller")
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/search")
    @ApiOperation("Search User by Name")
    public ResponseEntity getSearchResult(@RequestParam String searchString) {

        String regexp = "^" + searchString;

        List<String> resultList = userRepository.getSearchRegex(regexp);

        return ResponseEntity.ok().body(new SearchDto(resultList));
    }
}
