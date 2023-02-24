package com.winkly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winkly.entity.Links;
import com.winkly.entity.Problems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserDetailsDto {
    private String instaLink;
    private String twitterLink;
    private String username;
    private String name;
    private String bio;
    private Integer age;
    private String location;
    private List<Links> extraLinksList;
    private List<Problems> problemsList;
}
