package com.auth.studyprojectauthserver.Domain.Member.Service.impl;

import com.auth.studyprojectauthserver.Domain.Member.Service.inter.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.auth.studyprojectauthserver.Global.Util.AuthUtil.*;

/**
 * 로그아웃, 토큰 재발급 처리 관련 기능을 가지는 서비스 클래스의 구현체 입니다.
 *
 * @author : 황시준
 * @since  : 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * loginId를 가져오는 기능힙니다.
     * memberUuid를 통해 Redis에 저장된 USER_ID를 가져옵니다.
     * @param memberUuid
     * @return
     */
    @Override
    public String getLoginId(String memberUuid){
        return Objects.requireNonNull(redisTemplate.opsForHash().get(memberUuid, USER_ID.getValue()).toString());
    }

    /**
     * 토큰을 재발급 하는 기능입니다.
     * Redis에 저장된 기존 ACCESS_TOKEN을 지우고 새로은 ACCESS_TOKEN을 저장합니다.
     * @param memberUuid
     * @param accessToken 재발급 된 accessToken
     */
    @Override
    public void reissue(String memberUuid, String accessToken){
        redisTemplate.opsForHash().delete(memberUuid, ACCESS_TOKEN.getValue());
        redisTemplate.opsForHash().put(memberUuid, ACCESS_TOKEN.getValue(), accessToken);
    }


    /**
     * 로그아웃 하는 기능입니다.
     * Redis에 저장된 REFRESH_TOKEN, ACCESS_TOKEN, PRINCIPALS, USER_ID를 제거합니다/
     * @param memberUuid
     */
    @Override
    public void logout(String memberUuid){
        redisTemplate.opsForHash().delete(memberUuid, REFRESH_TOKEN.getValue());
        redisTemplate.opsForHash().delete(memberUuid, ACCESS_TOKEN.getValue());
        redisTemplate.opsForHash().delete(memberUuid, PRINCIPALS.getValue());
        redisTemplate.opsForHash().delete(memberUuid, USER_ID.getValue());
    }

    /**
     * 유저의 권한 정보를 가져오는 기능입니다.
     * Redus에 저장된 PRINCIPALS를 가져옵니다.
     * @param memberUuid
     * @return
     */
    @Override
    public String getPrincipals(String memberUuid){
        return Objects.requireNonNull(redisTemplate.opsForHash().get(memberUuid, PRINCIPALS.getValue()).toString());
    }
}
