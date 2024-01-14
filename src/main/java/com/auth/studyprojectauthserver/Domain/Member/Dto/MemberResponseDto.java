package com.auth.studyprojectauthserver.Domain.Member.Dto;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 전체 회원 정보를 담은 DTO입니다.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private String userId;
    private String email;
    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String phone;
    private List<String> roles;
    private Date createAt;
}
