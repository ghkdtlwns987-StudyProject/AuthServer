package com.auth.studyprojectauthserver.Global.Util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Key로 쓰이는 정보들에 대한 Enum 입니다.
 *
 * @author 황시준
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum AuthUtil {
    ACCESS_TOKEN("ACCESS_TOKEN"),
    REFRESH_TOKEN("REFRESH_TOKEN"),
    PRINCIPALS("PRINCIPALS"),
    USER_ID("USER_ID");

    private final String value;
}
