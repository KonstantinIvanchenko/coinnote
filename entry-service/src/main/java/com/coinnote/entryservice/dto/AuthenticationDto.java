package com.coinnote.entryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto {
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String userName;
}
