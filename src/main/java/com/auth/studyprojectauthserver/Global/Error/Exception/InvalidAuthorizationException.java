package com.auth.studyprojectauthserver.Global.Error.Exception;

import com.auth.studyprojectauthserver.Global.Error.ErrorCode;

public class InvalidAuthorizationException extends BuisinessException{
    public InvalidAuthorizationException(){
        super(ErrorCode.AUTHORIZATION_HEADER_INVALID);
    }

}
