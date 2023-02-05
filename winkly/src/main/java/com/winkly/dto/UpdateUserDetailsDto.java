package com.winkly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
