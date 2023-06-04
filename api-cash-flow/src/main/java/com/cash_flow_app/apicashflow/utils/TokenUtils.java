package com.cash_flow_app.apicashflow.utils;

import java.util.*;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security.LoginService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenUtils {
    private final static String ACCESS_TOKEN_SECRET = "c0b6a0bc15208bad5966234d25e5628882a0d105a49a1c55867ab0fb8fc25e1a";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;

    public static String createToken(String username){
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS*1000;
        Date expirationDAte = new Date(System.currentTimeMillis() + expirationTime);
        //Map<String, Object> extra = new HashMap<>();
        //extra.put("username", username);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDAte)
                //.addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token, LoginService loginService) {
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            UserDetails user = loginService.loadUserByUsername(username);
            // to review list authorities in third field
            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        } catch (JwtException e) {
            return null;
        }
    }
}
