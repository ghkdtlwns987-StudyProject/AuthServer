package com.auth.studyprojectauthserver.Domain.Member.Dto;

import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupResponseDto {
    private String userId;
    private String email;
    private String nickname;
    private String name;

    public static SignupResponseDto of(MemberEntity memberEntity){
        return new SignupResponseDto(memberEntity.getUserId(), memberEntity.getEmail(), memberEntity.getNickname(), memberEntity.getName());
    }
}
