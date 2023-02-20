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
    private Boolean tokenExpired = false;
    private String message;

    public JwtRequestDto(Boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public JwtRequestDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtRequestDto(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }
}
