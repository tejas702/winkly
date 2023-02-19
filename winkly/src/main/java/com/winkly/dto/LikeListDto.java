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
public class LikeListDto {
    private String name;
    private String username;
    private String profilePicture;
    private String reason;

    public LikeListDto(String name, String username, String profilePicture) {
        this.name = name;
        this.username = username;
        this.profilePicture = profilePicture;
    }
}
