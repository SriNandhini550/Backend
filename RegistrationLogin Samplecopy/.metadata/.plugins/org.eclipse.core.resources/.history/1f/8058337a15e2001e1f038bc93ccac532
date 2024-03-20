package com.dxc.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(Map<String, Object>extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+604800000))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Key getSigninKey() {
        String base64Key = "HIXcy+elQc3LoEY4UYx6lkJdg1+JoAtB6I5w80Bb9wc=";
        byte[] key = Decoders.BASE64.decode(base64Key);
        return Keys.hmacShaKeyFor(key);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
//    @Override
//    public String generateRefreshToken(String useremail) {
//        return Jwts.builder()
//                .setSubject(useremail)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // Expiry in 7 days
//                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
	@Override
	public String generateRefreshToken(String useremail) {
		// TODO Auto-generated method stub
		return null;
	}

}

