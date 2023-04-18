package hexlet.code.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;

@Component
public class JWTUtil {

    private final String secretKey;
    private final String issuer;
    private final Long expirationMsec;
    private final Long clockSkewSec;
    private final Clock clock;

    public JWTUtil(@Value("${jwt.issuer}") final String issuer,
                     @Value("${jwt.expiration-msec}") final Long expirationMsec,
                     @Value("${jwt.clock-skew-sec}") final Long clockSkewSec,
                     @Value("${jwt.secret}") final String secret) {
        this.secretKey = BASE64.encode(secret);
        this.issuer = issuer;
        this.expirationMsec = expirationMsec;
        this.clockSkewSec = clockSkewSec;
        this.clock = DefaultClock.INSTANCE;
    }

    public final String generateToken(final Map<String, Object> attributes) {
        return Jwts.builder()
                .signWith(HS256, secretKey)
                .setClaims(getClaims(attributes, expirationMsec))
                .compact();
    }

    private Claims getClaims(final Map<String, Object> attributes, final Long expiresInSec) {
        final Claims claims = Jwts.claims();
        claims.setIssuer(issuer);
        claims.setIssuedAt(clock.now());
        claims.putAll(attributes);
        if (expiresInSec > 0) {
            claims.setExpiration(new Date(System.currentTimeMillis() + expiresInSec));
        }
        return claims;
    }

    public final Map<String, Object> verify(final String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .setClock(clock)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
