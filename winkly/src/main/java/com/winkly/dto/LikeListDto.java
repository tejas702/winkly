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
    private String reason;

    public LikeListDto(String name, String username) {
        this.name = name;
        this.username = username;
    }
}
