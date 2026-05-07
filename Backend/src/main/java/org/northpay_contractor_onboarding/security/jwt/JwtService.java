package org.northpay_contractor_onboarding.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
  
  @Value("${JWT_SECRET}")
  private String secretKey;
  private static final long tokenExpiration = 1000 * 60 * 60;
  private static final long tokenRenewWindow = 1000 * 60 * 60 * 12;

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
    } catch (ExpiredJwtException e) {
      throw new RuntimeException(); // TODO: cambiar las runtimes por excepciones según el status a devolver y manejar desde el controlador de excepciones
    } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
      throw new RuntimeException("Token inválido o mal formado: " + e.getMessage());
    }
  }

  // Generación de token
  public String generateToken(String name, String email) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("name", name);

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
    if (expirationDate == null) throw new RuntimeException();
    return expirationDate.before(new Date());
  }

  // TODO: Renovación de token
}
