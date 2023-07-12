package com.auth.studyprojectauthserver.Domain.Member.Repository;

import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // SignupResponseDto save(RequestSignupRequestDto requestSignupRequestDto);
    MemberEntity findByUserId(String userId);
    Optional<MemberEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
