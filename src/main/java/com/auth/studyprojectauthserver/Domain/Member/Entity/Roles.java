package com.auth.studyprojectauthserver.Domain.Member.Entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
        USER(1, "User"),
        ADMIN(2, "Admin");
        private final int id;
        private final String name;
}

