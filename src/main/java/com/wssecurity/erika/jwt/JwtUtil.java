package com.wssecurity.erika.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wssecurity.erika.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

  private Key getKey() {
      byte[] keyBytes = Decoders.BASE64.decode("SECRETKEYquiEstSuperLongSaGrandJeTeLeDisMoiMonAmi");
      Key key = Keys.hmacShaKeyFor(keyBytes);
      return key;
  }


//La méthode de création de token restera toujours la même
  private String createToken(Map<String, Object> claims, String subject) {
  return Jwts
    .builder()
    .setClaims(claims)
    .setSubject(subject)
    .setIssuedAt(new Date(System.currentTimeMillis()))
    .setExpiration(new Date(System.currentTimeMillis() +
        1000 * 60 * 60 * 10))
    .signWith(getKey(), SignatureAlgorithm.HS256)
    .compact();
  }

//Utilisation de generateToken() pour ajouter le rôle  cô info dans mon token puis à la fin, on appelle createToken() pour empacter le tout
//??Aurait-il été possible d'ajouter le rôle dans createToken ?? Histoire de pas avoir à utiliser generateToken()    => oui
// public String generateToken(String username, String role) {
//   String jwt = Jwts.builder()
//           .subject(username)
//           .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // date d'expiration du token
//           .claim("role", role)
//           .signWith(generateKey())
//           .compact();
//   return jwt;
// }

  public String generateToken(UserDTO userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", userDetails.getRoles());
    return createToken(claims, userDetails.getName());
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parserBuilder()
      .setSigningKey(getKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Boolean validateToken(String token,
      UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) &&
        !isTokenExpired(token));
  }
}
