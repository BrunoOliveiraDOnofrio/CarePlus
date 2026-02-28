package com.example.careplus.config;

import com.example.careplus.service.S3Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

// Responsável por gerar e validar os tokens JWT
public class GerenciadorTokenJwt {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private Long jwtTokenValidity;

    public String getUsernameFromToken(String token){
        return getClaimForToken(token, Claims::getSubject);

    }

    public Date getExpirationDateFromToken(String token){
        return getClaimForToken(token, Claims::getExpiration);
    }

    public String generateToken(final Authentication authentication){
        // Para verificação de permissões
        final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder().setSubject(authentication.getName())
                .claim("roles", authorities)
                .signWith(parseSecret()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1_000)).compact();
    }

    public String generateToken(final Authentication authentication, Long userId, String nome, String cargo, String especialidade, String nomeSupervisor, String documento){

        final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder().setSubject(authentication.getName())
                .claim("roles", authorities)
                .claim("userId", userId)
                .claim("nome", nome)
                .claim("cargo", cargo)
                .claim("especialidade", especialidade)
                .claim("nomeSupervisor", nomeSupervisor)
                .claim("documento", documento)
                .signWith(parseSecret()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1_000)).compact();
    }

    public String getRolesFromToken(String token) {
        return getClaimForToken(token, claims -> claims.get("roles", String.class));
    }

    public Long getUserIdFromToken(String token) {
        return getClaimForToken(token, claims -> claims.get("userId", Long.class));
    }

    public String getNomeFromToken(String token) {
        return getClaimForToken(token, claims -> claims.get("nome", String.class));
    }

    public String getCargoFromToken(String token) {
        return getClaimForToken(token, claims -> claims.get("cargo", String.class));
    }

    public String getEspecialidadeFromToken(String token) {
        return getClaimForToken(token, claims -> claims.get("especialidade", String.class));
    }

    public <T> T getClaimForToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(parseSecret())
                .build()
                .parseClaimsJws(token).getBody();
    }

    private SecretKey parseSecret(){
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }


}