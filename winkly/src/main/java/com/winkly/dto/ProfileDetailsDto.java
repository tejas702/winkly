package com.winkly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDetailsDto {
    private String fbLink;
    private String instaLink;
    private String linktreeLink;
    private String linkedinLink;
    private String snapchatLink;
    private String twitterLink;
    private String username;
    private String email;
    private String name;
    private List<LikeListDto> likedYouList = new ArrayList<>();
    private List<LikeListDto> youLikedList = new ArrayList<>();
    private Boolean likeStatus;
    public ProfileDetailsDto(String fbLink, String instaLink, String linktreeLink, String linkedinLink,
                             String snapchatLink, String twitterLink, String username, String email, String name,
                             Boolean likeStatus) {
        this.fbLink = fbLink;
        this.instaLink = instaLink;
        this.linktreeLink = linktreeLink;
        this.linkedinLink = linkedinLink;
        this.snapchatLink = snapchatLink;
        this.twitterLink = twitterLink;
        this.username = username;
        this.email = email;
        this.name = name;
        this.likeStatus = likeStatus;
    }
}
