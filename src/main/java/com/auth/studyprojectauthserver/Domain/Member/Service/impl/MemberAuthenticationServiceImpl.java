package com.auth.studyprojectauthserver.Domain.Member.Service.impl;

import com.auth.studyprojectauthserver.Domain.Member.Dto.SignupRequestDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.SignupResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import com.auth.studyprojectauthserver.Domain.Member.Repository.MemberRepository;
import com.auth.studyprojectauthserver.Global.Error.Exception.MemberEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 회원 인증을 관리하는 서비스입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthenticationServiceImpl {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) throws Exception{
        if(memberRepository.existsByEmail(signupRequestDto.getEmail())){
            throw new MemberEmailAlreadyExistsException();
        }
        //SignupResponseDto signupResponseDto = memberRepository.save(requestSignupRequestDto);
        //return signupResponseDto;

        String encrpytedPassword = passwordEncoder.encode(signupRequestDto.getPwd());
        signupRequestDto.setPwd(encrpytedPassword);
        MemberEntity memberEntity = signupRequestDto.toEntity();

        /**
         * 비밀번호를 암호화 해야 함.
         */
        MemberEntity save = memberRepository.save(memberEntity);
        return SignupResponseDto.of(save);
    }
}
