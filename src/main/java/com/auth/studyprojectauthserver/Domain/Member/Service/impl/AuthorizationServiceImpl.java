package com.auth.studyprojectauthserver.Domain.Member.Service.impl;

import com.auth.studyprojectauthserver.Domain.Member.Dto.AuthorizationResopnseDto;
import com.auth.studyprojectauthserver.Domain.Member.Service.inter.AuthorizationService;
import com.auth.studyprojectauthserver.Global.Jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 인가 처리 관련 기능을 가지는 서비스 클래스 구현체 입니다.
 *
 * @author : 황시준
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationResopnseDto authorization(String token){
        jwtTokenProvider.isValidToken(token);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        List<String> authority = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthorizationResopnseDto(authentication.getName(), authority);
    }
}
