package com.auth.studyprojectauthserver.Global.Error.Exception;

import com.auth.studyprojectauthserver.Global.Error.ErrorCode;

/**
 * 이메일이 이미 존재한다고 했을 때 발생하는 예외 처리입니다.
 */
public class MemberEmailAlreadyExistsException extends BuisinessException{
    public MemberEmailAlreadyExistsException() {
        super(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
    }
}
