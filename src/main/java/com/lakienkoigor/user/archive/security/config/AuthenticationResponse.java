package com.lakienkoigor.user.archive.security.config;

import lombok.*;

/**
 * The Authentication response entity.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    /**
     * The token for authentication.
     */
    private String token;
}
