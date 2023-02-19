package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.SearchDto;
import com.winkly.entity.SearchResult;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity getSearchResult(@RequestParam(required = false) String searchString) {

        if (Objects.isNull(searchString)) {
            return ResponseEntity.ok().body(new SearchDto(new ArrayList<>()));
        }

        String regexp = "^" + searchString;

        List<SearchResult> resultList = new ArrayList<>();

        for (UserEntity result : userRepository.getSearchRegex(regexp)) {
            resultList.add(new SearchResult(result.getUsername(), result.getName(), result.getProfilePicture()));
        }

        return ResponseEntity.ok().body(new SearchDto(resultList));
    }
}