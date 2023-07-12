package com.auth.studyprojectauthserver.Domain.Member.Entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
        /*
        USER(1, "User"),
        ADMIN(2, "Admin");
        */
        USER("ROLE_USER", "유저"),
        ADMIN("ROLE_ADMIN", "관리자");
        private final String id;
        private final String name;
}

