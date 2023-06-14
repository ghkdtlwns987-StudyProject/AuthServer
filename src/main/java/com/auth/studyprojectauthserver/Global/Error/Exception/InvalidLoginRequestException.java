package com.auth.studyprojectauthserver.Global.Error.Exception;

import com.auth.studyprojectauthserver.Global.Error.ErrorCode;

public class InvalidLoginRequestException extends BuisinessException{
    public InvalidLoginRequestException(){
        super(ErrorCode.AUTHENTICATION_NOT_FOUND);
    }
}
