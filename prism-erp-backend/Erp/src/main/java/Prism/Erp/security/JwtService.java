package Prism.Erp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey; // 1. Configurado no application.properties ou variável de ambiente

    @Value("${jwt.expiration}")
    private long jwtExpiration; // Tempo de expiração

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extrai o email/nome do usuário do token
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails); // Gera um token sem claims extras
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims) // Define claims adicionais, se existirem
                .setSubject(userDetails.getUsername()) // Configura a identificação única do usuário
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de criação do token
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Configura a expiração
                .signWith(getSigningKey()) // Chave no formato compatível
                .compact(); // Gera o token JWT
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extract subject do token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // Valida o token
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Token está expirado?
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extrai o campo de expiração do token JWT
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Claims completas do token
        return claimsResolver.apply(claims); // Resolve o claim pedido
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Define a chave
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Método atualizado para usar uma chave compatível com `io.jsonwebtoken`
    private SecretKey getSigningKey() {
        // Converte a chave secreta em bytes compatíveis com algoritmos HMAC
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}