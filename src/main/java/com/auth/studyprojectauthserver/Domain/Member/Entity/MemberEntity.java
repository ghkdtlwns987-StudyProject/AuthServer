package com.auth.studyprojectauthserver.Domain.Member.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Member Table과 매핑되는 클래스입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "member")
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

    @Column(name = "Gender")
    private String gender;

    @Column(name = "name")
    private String name;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Roles")
    private int roles;

    @Column(name = "CreatedAt")
    private Date createAt;

    @Builder
    public MemberEntity(String email, String pwd, String name, String userId, String gender, String phone, int roles, Date createAt){
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.userId = userId;
        this.gender = gender;
        this.phone = phone;
        this.roles = roles;
        this.createAt = createAt;
    }

}