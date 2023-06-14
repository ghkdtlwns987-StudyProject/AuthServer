package com.auth.studyprojectauthserver.Domain.Member.Service.inter;

import com.auth.studyprojectauthserver.Domain.Member.Dto.AuthorizationResopnseDto;

/**
 * 인가 처리 관련 기능을 가지는 클래스입니다.
 * @author : 황시준
 * @since  : 1.0
 */
public interface AuthorizationService {

    /**
     *  토큰을 파싱하여 반환합니다.
     * @param token
     * @return 파싱된 토큰
     */
    AuthorizationResopnseDto authorization(String token);
}
