package com.auth.studyprojectauthserver.Global.Error.Exception;

import com.auth.studyprojectauthserver.Global.Error.ErrorCode;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String invalidToken) {
        super("Invalid token. " + invalidToken);
    }
}
