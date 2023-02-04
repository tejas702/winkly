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
public class JwtResponseDto {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;

    public JwtResponseDto(String accessToken, String refreshToken, Long id, String username, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
