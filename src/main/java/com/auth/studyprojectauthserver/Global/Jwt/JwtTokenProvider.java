package com.auth.studyprojectauthserver.Global.Jwt;

import com.auth.studyprojectauthserver.Domain.Member.Service.inter.UserService;
import com.auth.studyprojectauthserver.Global.Error.Exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(45).toMillis(); // 세션 지속 시간을 45분으로 설정
    private static final long REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(7).toMillis();    // RefreshToken 지속 시간을 7일로 설정

    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secretKey;

    //@Value("${jwt.secret}")
    //private String secretKey;

    private final UserDetailsService userDetailsService;
    /**
     * JWT를 생성하기 위해 HMAC-SHA 알고리즘으로 JWT에 서명한 키를 생성합니다.
     * @param accessKey JWT를 생성하기 위해 사용하는 secretKey 입니다.
     * @return 인코딩 된 secretKey를 기반으로 HMAC-SHA알고리즘으로 생성한 Key를 반환힙니다.
     * @author : 황시준
     * @since : 1.0
     */
    private Key getSecretKey(String accessKey){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰을 발급하는 기능입니다.
     *
     * @param loginId           회원의 loginId입니다/
     * @param roles             회원의 권한 목록입니다.
     * @param tokenExpireTime   토큰의 유효 시간입니다.
     * @return                  발급한 JWT토큰을 반환힙니다.
     * @author : 황시준
     * @since : 1.0
     */
    public String createToken(String loginId, List<String> roles, long tokenExpireTime){
        Claims claims = Jwts.claims().setSubject(loginId);
        claims.put("roles", roles);
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenExpireTime))
                .signWith(getSecretKey(secretKey), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * AccessToken을 생성합니다.
     * @param loginId
     * @param roles
     * @return
     * @author : 황시준
     * @since  : 1.0
     */
    public String createAccessToken(String loginId, List<String> roles){
        return createToken(loginId, roles, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * RefreshToken을 생성합니다.
     * @param loginId
     * @param roles
     * @return
     * @aythor : 황시준
     * @since  : 1.0
     */
    public String createRefreshToken(String loginId, List<String> roles){
        return createToken(loginId, roles, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * JWT 토큰의 만료 시간을 추출하기 위한 기능입니다.
     * @param token     JWT Token
     * @return          Token 만료 시간을 LocalDateTime 으로 변환
     * @author : 황시준
     * @since  : 1.0
     */
    public Date extractExpiredTime(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public String extractPrincipal(String token){
        token = removeTokenPrefix(token);
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody().toString();
    }

    /**
     * JWT 토큰의 loginId를 추출하는 기능입니다.
     * @param token     JWT Token
     * @return          Token의 loginId를 return 합니다.
     * @author : 황시준
     * @since  : 1.0
     */
    public String extractLoginId(String token){
        // token = removeTokenPrefix(token);
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Token의 Prefix를 제거합니다.
     * @param tokenWithPrefix
     * @return
     */
    public String removeTokenPrefix(String tokenWithPrefix){
        if(!tokenWithPrefix.startsWith(BEARER_PREFIX)){
            throw new InvalidTokenException(tokenWithPrefix);
        }
        return tokenWithPrefix.substring(BEARER_PREFIX.length());
    }

    /**
     * 유효한 토큰인지 검사하는 기능힙니다.
     * @param token
     * @return 유효한 토큰 : true
     * @return 유효하지 않은 토큰 : false
     */
    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey(secretKey))
                    .build()
                    .parseClaimsJws(token);

            log.info("token : {}", claimsJws);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 토큰을 재발급 합니다.
     * @param loginId
     * @param roles
     * @return
     */
    public String tokenReissue(String loginId, List<String> roles){
        log.info("loginId = {}", loginId);
        log.info("roles = {}", roles);

        String accessToken = createAccessToken(loginId, roles);

        log.info("Auth Server == Token Reissued, accessToken =  {}", accessToken);

        return accessToken;
    }

    /**
     * token에 부여된 권한 정보를 반환하는 메소드입니다.
     * @param token
     * @return : authorities
     */
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(extractLoginId(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }
}
