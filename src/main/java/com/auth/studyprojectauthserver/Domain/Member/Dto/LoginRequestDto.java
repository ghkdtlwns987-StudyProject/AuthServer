package com.auth.studyprojectauthserver.Domain.Member.Dto;

import lombok.*;

/**
 * 로그인 요청을 위한 DTO입니다.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String loginId;
    private String pwd;
}
