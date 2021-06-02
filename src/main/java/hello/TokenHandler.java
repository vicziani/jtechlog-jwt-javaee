package hello;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class TokenHandler {

    private static final String SECRET = "secret";

    private static final int EXPIRATION =  30 * 60 * 1000;

    public static final String ROLE_CLAIM = "role";


    public String generateToken(String username, String role) {
        try {
            long now = System.currentTimeMillis();

            return Jwts.builder()
                    .setSubject(username)
                .claim(ROLE_CLAIM, role)
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + EXPIRATION))
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                    .compact();
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Claims validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
