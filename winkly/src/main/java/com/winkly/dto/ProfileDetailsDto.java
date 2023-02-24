package com.winkly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winkly.entity.Links;
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
    private String instaLink;
    private String twitterLink;
    private String username;
    private String email;
    private String name;
    private String bio;
    private Integer age;
    private String location;
    private List<LikeListDto> likedYouList = new ArrayList<>();
    private List<LikeListDto> youLikedList = new ArrayList<>();
    private List<LikeListDto> matchedList = new ArrayList<>();
    private Boolean likeStatus;
    private String verifiedStatus;
    private List<Links> extraLinksList = new ArrayList<>();
    private String likedYouReason;
    private String youLikedReason;
    private String profilePicture;
    public ProfileDetailsDto(String instaLink, String twitterLink, String username, String email, String name,
                             String bio, Integer age, String location, Boolean likeStatus, String verifiedStatus, List<Links> extraLinksList,
                             String likedYouReason, String youLikedReason, String profilePicture) {
        this.instaLink = instaLink;
        this.twitterLink = twitterLink;
        this.username = username;
        this.email = email;
        this.name = name;
        this.bio = bio;
        this.age = age;
        this.location = location;
        this.likeStatus = likeStatus;
        this.verifiedStatus = verifiedStatus;
        this.extraLinksList = extraLinksList;
        this.likedYouReason = likedYouReason;
        this.youLikedReason = youLikedReason;
        this.profilePicture = profilePicture;
    }
}
