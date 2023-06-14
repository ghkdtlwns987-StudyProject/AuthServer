package com.auth.studyprojectauthserver.Global.Error.Exception;

public class InvalidAuthorizationHeaderException extends RuntimeException{
    private static final String MESSAGE = "Header 정보가 없거나 유효하지 않은 토큰입니다.";

    public InvalidAuthorizationHeaderException() {
        super(MESSAGE);
    }
}
