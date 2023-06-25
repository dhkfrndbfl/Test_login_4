package Test.Testlogin.jwt;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    private String secretkey;
    private  long validityInMilliseconds;

    public  JwtTokenProvider(@Value("{security.jwt.token.secret-key}") String secretkey, @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {
        this.secretkey = Base64.getEncoder().encodeToString("$2y$12$7Etikga5tYeG/X.5h3rGGO8kgkWSRPP8yCCXIi.Eyn27rqenIpoqG".getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    //토큰 생성
    public String createToken(String subject){
        Claims claims = Jwts.claims().setSubject(subject);


        Date now = new Date();

        Date validity = new Date(now.getTime()
        + validityInMilliseconds);
        log.info("secretkey = {}",secretkey);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,secretkey)
                .compact();

        //토큰에서 값 추출
       /* public String getSubject(String token){
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        }*/
    }

    //유효한 토큰인지 확인
    public boolean getSubject(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
