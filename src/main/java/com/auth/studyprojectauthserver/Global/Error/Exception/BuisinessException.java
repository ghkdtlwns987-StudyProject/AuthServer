package com.auth.studyprojectauthserver.Global.Error.Exception;

import com.auth.studyprojectauthserver.Global.Error.ErrorCode;
import com.auth.studyprojectauthserver.Global.Error.ErrorResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 비즈니스 요구사항에 따른 예외 처리입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Getter
public class BuisinessException extends RuntimeException{
    private ErrorCode errorCode;
    private List<ErrorResponse.FieldError> errors = new ArrayList<>();

    public BuisinessException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public BuisinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BuisinessException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
