package com.auth.studyprojectauthserver.Domain.Member.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Member Table과 매핑되는 클래스입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "member_temp")
@RequiredArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String pwd;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "name")
    private String name;

    @Column(name = "Phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "Roles")
    private Roles roles;

    @Column(name = "CreatedAt")
    private Date createAt;

    @Builder
    public MemberEntity(String email, String pwd, String nickname, String name, String userId, String phone, Roles roles, Date createAt){
        this.email = email;
        this.pwd = pwd;
        this.nickname = nickname;
        this.name = name;
        this.userId = userId;
        this.phone = phone;
        this.roles = roles;
        this.createAt = createAt;
    }

    public void update(String password, String nickname, String phone){
        this.pwd = password;
        this.nickname = nickname;
        this.phone = phone;
    }
}