package com.winkly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtRequestDto {
    private String accessToken;
    private String refreshToken;
    private String message;

    public JwtRequestDto(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }

    public JwtRequestDto(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }
}
