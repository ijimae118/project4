package com.laundy.laundrybackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private String username;

    public JwtResponseDTO(String accessToken, String username) {
        this.token = accessToken;
        this.username = username;
    }
}
