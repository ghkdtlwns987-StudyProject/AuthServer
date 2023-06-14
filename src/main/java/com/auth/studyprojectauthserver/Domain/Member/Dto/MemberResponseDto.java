package com.auth.studyprojectauthserver.Domain.Member.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 전체 회원 정보를 담은 DTO입니다.
 */
@Getter
@Setter
public class MemberResponseDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createAt;

    private String encPwd;
}
