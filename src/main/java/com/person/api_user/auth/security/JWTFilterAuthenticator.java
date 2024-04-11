package com.person.api_user.auth.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.person.api_user.auth.data.DetailUserData;
import com.person.api_user.domain.user.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilterAuthenticator extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRACAO = 600_000;
    public static final String TOKEN_SENHA = "Senha-exemplo-para-desenvolvimento";

    private final AuthenticationManager authenticationManager;

    public JWTFilterAuthenticator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
                ));

        } catch (Exception e) {
            throw new RuntimeException("Falha ao autenticar usuário.", e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
    HttpServletResponse response, 
    FilterChain chain, 
    Authentication authResult) throws IOException {
        
        DetailUserData userData = (DetailUserData) authResult.getPrincipal();

        String token  = JWT.create()
            .withSubject(userData.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO))
            .sign(Algorithm.HMAC512(TOKEN_SENHA));

        response.getWriter().write(token);
        response.getWriter().flush();    
    }

}
