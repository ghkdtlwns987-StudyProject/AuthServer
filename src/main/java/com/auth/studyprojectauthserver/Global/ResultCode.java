package com.auth.studyprojectauthserver.Global;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 요청 성공 시 응답하는 코드 모임입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // Member
    REGISTER_SUCCESS(200, "M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(200, "M002", "로그인 되었습니다."),
    REISSUE_SUCCESS(200, "M003", "재발급 되었습니다."),
    LOGOUT_SUCCESS(200, "M004", "로그아웃 되었습니다."),
    GET_MY_INFO_SUCCESS(200, "M005", "내 정보 조회 완료");

    private int status;
    private final String code;
    private final String message;
}