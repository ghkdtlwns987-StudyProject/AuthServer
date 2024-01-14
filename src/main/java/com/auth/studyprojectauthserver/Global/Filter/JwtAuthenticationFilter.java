package com.auth.studyprojectauthserver.Global.Filter;

import com.auth.studyprojectauthserver.Domain.Member.Dto.LoginRequestDto;
import com.auth.studyprojectauthserver.Global.Error.Exception.InvalidLoginRequestException;
import com.auth.studyprojectauthserver.Global.Jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import static com.auth.studyprojectauthserver.Global.Util.AuthUtil.*;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String UUID_HEADER = "UUID_HEADER";
    private static final String X_EXPIRE_HEADER = "X-Expire";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Login 시도시 발생하는 기능 입니다.
     * 사용자가 입력한 loginId와 pwd를 기반으로
     * UsernamePasswordAuthenticationToken을 발급하고 authenticationManager에 인가를 위임합니다.
     *
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return authenticaztionManager에게 인가를 위임하여 반환된 결과입니다.
     * @throws AuthenticationException Spring Security에서 발생하는 예외입니다.
     * @throws InvalidLoginRequestException 커스텀 에러 메시지 입니다.
     * @author : 황시준
     * @since : 1.0
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = mapper.readValue(request.getInputStream(), LoginRequestDto.class);
            log.info("Auth Server == Attempt Authentication");
            log.info("loginId = {}", loginRequestDto.getLoginId());
        } catch (IOException e) {
            throw new InvalidLoginRequestException();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getLoginId(),
                loginRequestDto.getPassword()
        );

        log.info("authenticationToken.getName={}", authenticationToken.getName());
        log.info("authenticationToken.getAuthorities={}", authenticationToken.getAuthorities());
        log.info("authenticationToken.getPrincipal={}", authenticationToken.getPrincipal().toString());
        log.info("authenticationToken.getDetails={}", authenticationToken.getDetails());

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * 인증 객체를 JwtTokenProvider에게 전달하여 AccessToken을 발급합니다.
     *
     * @param authentication 인증 객체입니다.
     * @return               JWT형식의 AccessToken을 발급합니다.
     * @author : 황시준
     * @since : 1.0
     */
    private String getAccessToken(Authentication authentication){
        return jwtTokenProvider.createAccessToken(
                authentication.getName(),
                authentication.getAuthorities().stream().
                        map(GrantedAuthority::getAuthority).
                        collect(Collectors.toList())
        );
    }

    /**
     * RefreshToken을 가져옵니다.
     * @param authentication 인증 객체입니다.
     * @return               JWT형식의 AccessToken을 발급합니다.
     */
    private String getRefreshToken(Authentication authentication){
        return jwtTokenProvider.createRefreshToken(
                authentication.getName(),
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 인증 성공 시 동작하는 후처리 메소드 입니다.
     * JWT토큰을 발급 하고 Http Header Authorization 필드에 accessToken을 반환합니다.
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param chain     FilterChain
     * @param auth      인증 객체 입니다.
     * method.
     * @throws IOException  IO 관련 예외
     * @throws ServletException Servlet관련 예외
     * @author : 황시준
     * @since : 1.0
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth
    ) throws IOException, ServletException{
        log.info("Auth Server == Authentication Success");
        String accessToken = getAccessToken(auth);
        String refreshToken = getRefreshToken(auth);
        // refreshToken를 Redis에 저장
        long expiredTime = jwtTokenProvider.extractExpiredTime(accessToken).getTime();
        long currentTime = new Date().getTime();

        String memberUUID = UUID.randomUUID().toString();

        redisTemplate.opsForHash().put(memberUUID, REFRESH_TOKEN.getValue(), refreshToken);
        redisTemplate.opsForHash().put(memberUUID, ACCESS_TOKEN.getValue(), accessToken);
        redisTemplate.opsForHash().put(memberUUID, USER_ID.getValue(), auth.getName());
        redisTemplate.opsForHash().put(memberUUID, PRINCIPALS.getValue(), auth.getAuthorities().toString());

        redisTemplate.expire(memberUUID, expiredTime - currentTime, TimeUnit.MILLISECONDS);     // Redis에 저장할 세션 만료시간 설정

        response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
        response.addHeader(UUID_HEADER, memberUUID);
        response.addHeader(X_EXPIRE_HEADER, String.valueOf(expiredTime));
    }
    /**
     * 인증 실패 시 동작 하는 후처리 메소드 입니다.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param failed Spring Security의 인증 실패 예외
     * @throws IOException IO 관련 예외
     * @throws ServletException Servlet 관련 예외
     * @author 황시준
     * @since 1.0
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException{
        log.error("login failed={}", failed.toString());
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
