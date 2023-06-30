package com.auth.studyprojectauthserver.Domain.Member.Dto;

import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

/**
 * 전체 회원 정보를 담은 DTO입니다.
 */
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto {
    private String userId;
    private String email;
    private String nickname;
    private String name;
    private String phone;
    private int roles;
    private Date createAt;

    public static MemberResponseDto of(MemberEntity memberEntity){
        return new MemberResponseDto(
                memberEntity.getUserId(),
                memberEntity.getEmail(),
                memberEntity.getNickname(),
                memberEntity.getName(),
                memberEntity.getPhone(),
                memberEntity.getRoles(),
                memberEntity.getCreateAt()
        );
    }
}
