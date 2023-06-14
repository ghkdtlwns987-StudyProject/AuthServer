package com.auth.studyprojectauthserver.Domain.Member.Controller;

import com.auth.studyprojectauthserver.Domain.Member.Dto.LogoutRequestDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.ResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Service.inter.AuthenticationService;
import com.auth.studyprojectauthserver.Global.Error.Exception.InvalidAuthorizationException;
import com.auth.studyprojectauthserver.Global.Error.Exception.InvalidAuthorizationHeaderException;
import com.auth.studyprojectauthserver.Global.Jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static com.auth.studyprojectauthserver.Global.Util.AuthUtil.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationService authenticationService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String UUID_HEADER = "UUID_HEADER";
    private static final String X_EXPIRE_HEADER = "X-Expire";
    /**
     * 토큰을 재발합하는 기능입니다.
     * 추후 Redis에 저장된 데이터를 기반으로 UUID를 기반으로 사용자 정보를 추출할 예정입니다.
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @return          응답 결과를 담은 DTO
     * @author : 황시준
     * @since  : 1.0
     */
    @PostMapping("/reissue")
    public ResponseDto<Void> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String accessToken = request.getHeader(AUTHORIZATION);
        String memberUuid = request.getHeader(UUID_HEADER);

        log.info("Auth Server == Token Reissue Called");

        if(isValidHeader(accessToken, memberUuid)){
            throw new InvalidAuthorizationException();
        }

        // Key가 유효 한지 검사
        if(!isValidKey(memberUuid)){
            log.info("memberUUID Not Found");
            return ResponseDto.<Void>builder()
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .errorMessages(List.of("[Error] : User Already Logout"))
                    .build();
        }

        // RefreshToken이 유효한지 검사
        if(!isValidRefreshToken(memberUuid)){
            return ResponseDto.<Void>builder()
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .errorMessages(List.of("[Error] : Refresh Token is Already Expired"))
                    .build();
        }


        String loginId = authenticationService.getLoginId(memberUuid);
        String principal = authenticationService.getPrincipals(memberUuid);
        log.info("loginId = {}", loginId);
        log.info("principal = {}", principal);

        // 권한을 꺼내오는 로직 추가
        List<String> roles = extractUserRoles(principal);
        String reissueToken = jwtTokenProvider.tokenReissue(accessToken, roles);

        authenticationService.reissue(memberUuid, reissueToken);

        long expiredTime = jwtTokenProvider.extractExpiredTime(reissueToken).getTime();
        long currentTime = new Date().getTime();

        response.addHeader(AUTHORIZATION, reissueToken);
        response.addHeader(UUID_HEADER, memberUuid);
        response.addHeader(X_EXPIRE_HEADER, String.valueOf(expiredTime));

        redisTemplate.expire(memberUuid, expiredTime - currentTime, TimeUnit.MILLISECONDS);     // Redis에 저장할 세션 만료시간 설정


        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * logout 기능을 수행합니다.
     * logout 은 Redis에 저장된 데이터를 삭제합니다.
     * @param request : LogoutRequestDto
     * @param accessToken : AuthorizationHeader
     * @return : ResponseDto<Void>
     * @author : 황시준
     * @since : 1.0
     */
    @PostMapping("/logout")
    public ResponseDto<Void> logout(
            @RequestBody LogoutRequestDto request,
            @RequestHeader(name = "Authorization") String accessToken
    ){
        String uuid = request.getKey();
        log.info("Auth Server == Logout Called");

        if(isValidHeader(accessToken, uuid)){
            throw new InvalidAuthorizationHeaderException();
        }

        if(isValidKey(uuid)){
            authenticationService.logout(uuid);

            return ResponseDto.<Void>builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .build();
        }
        return ResponseDto.<Void>builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .errorMessages(List.of("이미 로그아웃 된 사용자 입니다."))
                .build();
    }

    private boolean isValidHeader(String accessToken, String memberUuid){
        return Objects.isNull(accessToken) || Objects.isNull(memberUuid) || !accessToken.startsWith("Bearer ")
                || !jwtTokenProvider.isValidToken(accessToken.substring(7));
    }

    private List<String> extractUserRoles(String principals){
        return Arrays.asList(principals.replaceAll("[\\[\\]]", "").split(", "));
    }

    private boolean isValidKey(String memberUuid){
        return !redisTemplate.opsForHash().keys(memberUuid).isEmpty();
    }

    private boolean isValidRefreshToken(String memberUuid){
        String refreshToken = Objects.requireNonNull(redisTemplate.opsForHash()
                .get(memberUuid, REFRESH_TOKEN.getValue()))
                .toString();

        long expiredTime = jwtTokenProvider.extractExpiredTime(refreshToken).getTime();

        long now = new Date().getTime();
        return (expiredTime - (now / 1000)) > 0;

    }
}
