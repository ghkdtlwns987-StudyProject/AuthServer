package com.auth.studyprojectauthserver.Domain.Member.Dto;

import com.auth.studyprojectauthserver.Global.ResultCode;
import lombok.Getter;

/**
 * 결과값을 Response하는 DTO입니다.
 *
 * 출력 형식은 다음과 같게 설계했습니다.
 * {
 *     "status" : 200,
 *     "code" : "M001",
 *     "message" : "회원가입 성공",
 *     "data" : {
 *         "email" : "ghkdtlwns987@naver.com",
 *         "name" : "ghkdtlwns987"
 *     }
 * }
 */
@Getter
public class ResultResponse {
    private int status;
    private String code;
    private String message;
    private Object data;

    public static ResultResponse of(ResultCode resultCode, Object data){
        return new ResultResponse(resultCode, data);
    }

    public ResultResponse(ResultCode resultCode, Object data){
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }
}
