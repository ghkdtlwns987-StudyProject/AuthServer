package com.auth.studyprojectauthserver.Domain.Member.Service.inter;


/**
 * 로그아웃, 토큰 재발급 기능을 가지는 서비스 인터페이스 입니다.
 *
 * @author : 황시준
 * @since : 1.0
 */
public interface AuthenticationService {

    /**
     * 회원의 loginId를 Redis에서 가져옵니다.
     * @param memberUuid
     * @return Redis에 저장된 uuid
     */
    String getLoginId(String memberUuid);

    /**
     * 회원의 권한 정보를 가져오는 기능
     * @param memberUuid
     * @return Redis에 저장된 정보
     * @author : 황시준
     * @since : 1.0
     */
    String getPrincipals(String memberUuid);

    /**
     * 토큰 재발급 이후 처리를 위한 기능
     * @param memberUuid
     * @param reissueToken 재발급 된 accessToken
     * @author : 황시준
     * @since : 1.0
     */
    void reissue(String memberUuid, String reissueToken);


    /**
     * 로그아웃 처리를 위한 기능합니다.
     * Redis에 접근해 해당 유저의 정보를 삭제 합니다.
     * @param memberUuid
     */
    void logout(String memberUuid);
}
