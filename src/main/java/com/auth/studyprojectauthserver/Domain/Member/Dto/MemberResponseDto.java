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
    private String pwd;
    private String nickname;
    private String name;
    private String phone;
    private List<String> roles;
    private Date createAt;

    // DELETE 예약
    /*
    public static MemberResponseDto of(MemberEntity memberEntity){
        return new MemberResponseDto(
                memberEntity.getUserId(),
                memberEntity.getEmail(),
                memberEntity.getPwd(),
                memberEntity.getNickname(),
                memberEntity.getName(),
                memberEntity.getPhone(),
                memberEntity.getRoles(),
                memberEntity.getCreateAt()
        );
    }
     */
}
