package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.UpdateUserDetailsDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

        List<UserEntity> resultList = userRepository.getSearchRegex(regexp);

        String content = resultList.toString();

        return ResponseEntity.ok().body(new MessageInfoDto(content));
    }
}
