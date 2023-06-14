package com.auth.studyprojectauthserver.Domain.Member.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {
    private String key;
}
