package com.custManagent.auth;

import java.io.Serial;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.custManagent.dto.CustomerDto;
import com.custManagent.entity.Customer;
import com.custManagent.entity.JwtResponse;
import com.custManagent.repository.CustomerRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 90L * 24 * 60 * 60;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.type}")
    private String jwtType;

    @Autowired
    private CustomerRepository customerRepository;

    // Properly decode and generate a secure signing key
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getIdFromToken(String token) { //get role id (jti)
        return getClaimFromToken(token, Claims::getId);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) 
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {    
        return false;
    }

    public JwtResponse generateToken(CustomerDto custDetails) {
        Map<String, Object> claims = new HashMap<>();
        return JwtResponse.builder()
                .jwttoken(doGenerateToken(claims, custDetails))
                .build();
    }

    private String doGenerateToken(Map<String, Object> claims, CustomerDto subject) {
        return Jwts.builder()
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) 
                .setHeaderParam("type", jwtType)
                .setSubject(subject.getPhoneNumber())
                .setId(String.valueOf(subject.getRoleId())) 
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000L)) 
                .compact();
    }

    public Boolean validateToken(String token, Customer custDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(custDetails.getEmail()) && !isTokenExpired(token));
    }
  //checking the token expire or not
  	public String authentication(String token) {
          try { 
              if (isTokenExpired(token)) {
                  return "FAILURE";
              }
              return "SUCCESS";
          } catch (Exception e) {
              return "FAILURE"; 
          }
      }

    
}
