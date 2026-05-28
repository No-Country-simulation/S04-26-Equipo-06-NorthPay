package org.northpay_contractor_onboarding.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.northpay_contractor_onboarding.dto.jwt.JwtClaimsDTO;
import org.northpay_contractor_onboarding.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  
  @Value("${JWT_SECRET}")
  private String secretKey;
  private static final long tokenExpiration = 1000 * 60 * 60;
  // private static final long tokenRenewWindow = 1000 * 60 * 60 * 12;

  private Key getSecretKeyFromToken() {
    byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);

    return Keys.hmacShaKeyFor(secretKeyBytes);
  }

  private Claims defineBuilderAndReturnClaims(String token) {
    try {
      return Jwts.parserBuilder()
        .setSigningKey(getSecretKeyFromToken())
      .build()
        .parseClaimsJws(token)
      .getBody();
    } catch (UnsupportedJwtException | IllegalArgumentException e) {
      throw new InvalidTokenException("Invalid: " + e.getMessage());
    }
  }

  // Generación de token
  public String generateToken(JwtClaimsDTO claims, String email) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("name", claims.name());
    extraClaims.put("role", claims.role());
    extraClaims.put("type", claims.type());

    long issuedAtInMs = System.currentTimeMillis();

    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(email)
      .setIssuedAt(new Date(issuedAtInMs))
      .setExpiration(new Date(issuedAtInMs + tokenExpiration))
      .signWith(getSecretKeyFromToken())
    .compact();
  }

  // Obtención de claims
  public <T> T getClaim(String token, Function<Claims, T> claimGetter) {
    final Claims claims = defineBuilderAndReturnClaims(token);
    return claimGetter.apply(claims);
  }

  // Validaciones
  public boolean isTokenExpired(String token) {
    Date expirationDate = getClaim(token, claims -> claims.getExpiration());
    if (expirationDate == null) throw new InvalidTokenException("Token wasn't generated right, no expiration detected");
    return expirationDate.before(new Date());
  }

  // TODO: Renovación de token
}
