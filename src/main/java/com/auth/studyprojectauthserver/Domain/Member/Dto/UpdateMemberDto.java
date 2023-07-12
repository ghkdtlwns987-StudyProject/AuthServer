package com.auth.studyprojectauthserver.Domain.Member.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberDto {
    private String pwd;
    private String nickname;
    private String phone;

    @Builder
    public UpdateMemberDto(String pwd, String nickname, String phone){
        this.pwd = pwd;
        this.nickname = nickname;
        this.phone = phone;
    }
}
