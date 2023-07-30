package com.lakienkoigor.user.archive.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The JSON Web Token service.
 */
@Service
public class JwtService {

    private static final String SECRET_KEY = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    /**
     * Extract username
     *
     * @param token the String
     * @return the String
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract claim
     *
     * @param <T>            the type generic
     * @param token          the type String
     * @param claimsResolver the type Function<Claims,T>
     * @return the type generic
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate token string.
     *
     * @param userDetails the type UserDetails
     * @return the type String
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate token string
     *
     * @param extraClaims the type Map<String, Object>
     * @param userDetails the type UserDetails
     * @return the type String
     */
    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checking valid token and check expiration
     *
     * @param token       the type String
     * @param userDetails the type UserDetails
     * @return the type  boolean
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final var username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * The extracting token and check expiration
     *
     * @param token the type String
     * @return the type  boolean
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * The extracting claims and check expiration
     *
     * @param token the type String
     * @return the type  Date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * The extracting all claims
     *
     * @param token the type String
     * @return the type  Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Getting the valid key for log in
     *
     * @return the type  Key
     */
    private Key getSingInKey() {
        final var keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
