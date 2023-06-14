package com.auth.studyprojectauthserver.Domain.Member.Dto;

import java.util.List;

public class AuthorizationResopnseDto {
    private String loginId;
    private List<String> roles;


    public String getLoginId(){
        return this.loginId;
    }

    public List<String> getRoles(){
        return this.roles;
    }

    private AuthorizationResopnseDto(){

    }

    public AuthorizationResopnseDto(String loginId, List<String> roles){
        this.loginId = loginId;
        this.roles = roles;
    }
}
