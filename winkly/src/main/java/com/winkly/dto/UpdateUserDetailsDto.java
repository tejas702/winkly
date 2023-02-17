package com.winkly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winkly.entity.Links;
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
    private String fbLink;
    private String instaLink;
    private String linktreeLink;
    private String linkedinLink;
    private String snapchatLink;
    private String twitterLink;
    private String username;
    private String name;
    private String bio;
    private List<Links> extraLinks;
}
